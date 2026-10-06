/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;

import java.time.LocalDate;
import java.util.List;

/** Service handling member registration and Stage 1 checkout delegation. */
public final class MemberService {

    /** DAO for member entities. */
    private final MemberDao memberDao;

    /** DAO for book entities. */
    private final BookDao bookDao;

    /** DAO for loan entities. */
    private final LoanDao loanDao;

    /**
     * Creates a new MemberService.
     *
     * @param memberDao the member DAO
     * @param bookDao the book DAO
     * @param loanDao the loan DAO
     */
    public MemberService(MemberDao memberDao, BookDao bookDao, LoanDao loanDao) {
        this.memberDao = memberDao;
        this.bookDao = bookDao;
        this.loanDao = loanDao;
    }

    /**
     * Registers a new member.
     *
     * @param member the member to register
     * @return the registered member
     */
    public Member register(Member member) {
        memberDao.create(member);
        return member;
    }

    /**
     * Finds all registered members.
     *
     * @return list of all members
     */
    public List<Member> findAll() {
        return memberDao.findAll();
    }

    /**
     * Performs a book checkout for a member using the shared loan policy.
     *
     * @param memberId the ID of the member
     * @param bookId the ID of the book
     * @return the created loan
     * @throws NotFoundException if the member or book is not found
     * @throws IllegalStateException if no copies are available
     */
    public Loan checkout(int memberId, int bookId) {
        Member member = memberDao.findById(memberId);
        Book book = bookDao.findById(bookId);
        if (member == null) {
            throw new NotFoundException("Member not found: " + memberId);
        }
        if (book == null) {
            throw new NotFoundException("Book not found: " + bookId);
        }
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies of book " + bookId);
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookDao.update(book);

        LocalDate today = LocalDate.now();
        Loan loan = new Loan(member, book, today, LoanPolicy.dueDate(today));
        loanDao.create(loan);
        return loan;
    }
}
