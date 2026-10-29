/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;

import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/** Service handling loan records, checkouts, and return operations. */
public final class LoanService {

    /** DAO for accessing loan records. */
    private final LoanDao loanDao;

    /** DAO for accessing member records. */
    private final MemberDao memberDao;

    /** Service for book inventory operations. */
    private final BookService bookService;

    /** Clock for date operations. */
    private final Clock clock;

    /**
     * Creates a new LoanService.
     *
     * @param loanDao the loan DAO
     * @param memberDao the member DAO
     * @param bookService the book service
     * @param clock the clock provider
     */
    public LoanService(LoanDao loanDao, MemberDao memberDao, BookService bookService, Clock clock) {
        this.loanDao = loanDao;
        this.memberDao = memberDao;
        this.bookService = bookService;
        this.clock = clock;
    }

    /**
     * Finds all loans with open overdue fees dynamically accrued.
     *
     * @return list of all loans
     */
    public List<Loan> findAll() {
        return withAccruedFees(loanDao.findAll());
    }

    /**
     * Checks out a book to a member in a single transaction.
     *
     * @param memberId the ID of the borrowing member
     * @param bookId the ID of the book to borrow
     * @return the created loan
     * @throws NotFoundException if the member or book does not exist
     * @throws IllegalStateException if no copies of the book are available
     */
    public Loan checkout(int memberId, int bookId) {
        Member member = memberDao.findById(memberId);
        if (member == null) {
            throw new NotFoundException("Member not found: " + memberId);
        }
        Book book = bookService.findById(bookId);
        if (book == null) {
            throw new NotFoundException("Book not found: " + bookId);
        }

        LocalDate today = LocalDate.now(clock);
        Loan loan = new Loan(member, book, today, LoanPolicy.dueDate(today));

        try {
            return loanDao.transaction(() -> {
                bookService.borrow(bookId);
                loanDao.create(loan);
                return loan;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Processes the return of a borrowed book in a single transaction.
     *
     * @param loanId the ID of the loan to return
     * @return the updated loan
     * @throws NotFoundException if the loan does not exist
     * @throws IllegalStateException if the loan was already returned
     */
    public Loan returnLoan(int loanId) {
        Loan loan = loanDao.findById(loanId);
        if (loan == null) {
            throw new NotFoundException("Loan not found: " + loanId);
        }
        if (loan.isReturned()) {
            throw new IllegalStateException("Loan already returned: " + loanId);
        }

        LocalDate today = LocalDate.now(clock);
        loan.setReturned(true);
        loan.setReturnDate(today);

        if (today.isAfter(loan.getDueDate())) {
            loan.setOverdueFee(accruedFee(loan, today));
        }

        try {
            return loanDao.transaction(() -> {
                loanDao.update(loan);
                bookService.returnCopy(loan.getBook().getId());
                return loan;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns all open loans that are past their due date.
     *
     * @return list of overdue loans
     */
    public List<Loan> overdueLoans() {
        LocalDate today = LocalDate.now(clock);
        return findAll().stream()
                .filter(loan -> !loan.isReturned() && loan.getDueDate().isBefore(today))
                .toList();
    }

    /**
     * Sets accrued fees on open overdue loans without persisting them.
     *
     * @param loans list of loans
     * @return list of loans with fees accrued
     */
    private List<Loan> withAccruedFees(List<Loan> loans) {
        LocalDate today = LocalDate.now(clock);
        for (Loan loan : loans) {
            if (!loan.isReturned() && loan.getDueDate().isBefore(today)) {
                loan.setOverdueFee(accruedFee(loan, today));
            }
        }
        return loans;
    }

    /**
     * Calculates the overdue fee for a loan as of a specific date.
     *
     * @param loan the overdue loan
     * @param today the current date
     * @return calculated overdue fee
     */
    private double accruedFee(Loan loan, LocalDate today) {
        return ChronoUnit.DAYS.between(loan.getDueDate(), today) * LoanPolicy.FEE_PER_DAY;
    }

    /**
     * Calculates the overdue fee for a loan as of the current clock date.
     *
     * @param loan the overdue loan
     * @return calculated overdue fee
     */
    private double accruedFee(Loan loan) {
        return accruedFee(loan, LocalDate.now(clock));
    }
}
