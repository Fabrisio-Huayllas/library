/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/** Service handling loan records and loan return operations. */
public final class LoanService {

    /** Default loan period in days. */
    public static final int DUE_DAYS = 21;

    /** DAO for accessing loan records. */
    private final LoanDao loanDao;

    /** DAO for accessing book inventory. */
    private final BookDao bookDao;

    /**
     * Creates a new LoanService.
     *
     * @param loanDao the loan DAO
     * @param bookDao the book DAO
     */
    public LoanService(LoanDao loanDao, BookDao bookDao) {
        this.loanDao = loanDao;
        this.bookDao = bookDao;
    }

    /**
     * Finds all loans in the database.
     *
     * @return list of all loans
     * @throws SQLException if a database access error occurs
     */
    public List<Loan> findAll() throws SQLException {
        return loanDao.findAll();
    }

    /**
     * Processes the return of a borrowed book.
     *
     * @param loanId the ID of the loan to return
     * @return the updated loan
     * @throws SQLException if a database access error occurs
     */
    public Loan returnLoan(int loanId) throws SQLException {
        Loan loan = loanDao.findById(loanId);
        if (loan == null || loan.isReturned()) {
            return loan;
        }

        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());

        LocalDate due = loan.getDueDate();
        LocalDate today = LocalDate.now();
        if (today.isAfter(due)) {
            long daysOverdue = ChronoUnit.DAYS.between(due, today);
            loan.setOverdueFee(daysOverdue * 1.0);
        }

        loanDao.update(loan);

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookDao.update(book);

        return loan;
    }

    /**
     * Returns all open loans that are past their due date.
     *
     * @return list of overdue loans
     * @throws SQLException if a database access error occurs
     */
    public List<Loan> overdueLoans() throws SQLException {
        LocalDate today = LocalDate.now();
        return loanDao.findAll().stream()
            .filter(loan -> !loan.isReturned() && loan.getDueDate().isBefore(today))
            .toList();
    }
}
