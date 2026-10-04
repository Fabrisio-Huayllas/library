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

public final class MemberService {

    private final MemberDao memberDao;
    private final BookDao bookDao;
    private final LoanDao loanDao;

    public MemberService(MemberDao memberDao, BookDao bookDao, LoanDao loanDao) {
        this.memberDao = memberDao;
        this.bookDao = bookDao;
        this.loanDao = loanDao;
    }

    public Member register(Member member) {
        memberDao.create(member);
        return member;
    }

    public List<Member> findAll() {
        return memberDao.findAll();
    }

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
