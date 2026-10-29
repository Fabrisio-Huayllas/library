/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.db.Database;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.service.BookService;
import cl.ucn.disc.arqsist.library.service.LoanService;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Verifies that open overdue loans accurately compute and expose accrued overdue fees. */
class AccruedFeeTest {

    @Test
    void accruedFeesAreCalculatedForOpenOverdueLoans() throws Exception {
        Database db = new Database("jdbc:sqlite::memory:");
        BookDao bookDao = new BookDao(db.connectionSource());
        MemberDao memberDao = new MemberDao(db.connectionSource());
        LoanDao loanDao = new LoanDao(db.connectionSource());
        BookService bookService = new BookService(bookDao);

        Clock clock = Clock.fixed(Instant.parse("2026-01-15T10:00:00Z"), ZoneOffset.UTC);
        LoanService loanService = new LoanService(loanDao, memberDao, bookService, clock);

        Book book = new Book("Refactoring", "Martin Fowler", "9780201485677", 2);
        bookDao.create(book);

        Member member = new Member("Ada Lovelace", "ada@example.com");
        memberDao.create(member);

        Loan overdue = new Loan(member, book, LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 22));
        loanDao.create(overdue);

        Loan active = new Loan(member, book, LocalDate.of(2026, 1, 10), LocalDate.of(2026, 1, 31));
        loanDao.create(active);

        List<Loan> loans = loanService.findAll();
        Loan foundOverdue = loans.stream().filter(l -> l.getId() == overdue.getId()).findFirst().orElseThrow();
        Loan foundActive = loans.stream().filter(l -> l.getId() == active.getId()).findFirst().orElseThrow();

        assertEquals(24.0, foundOverdue.getOverdueFee(), 0.001, "Overdue loan should accrue 24.0 fee");
        assertEquals(0.0, foundActive.getOverdueFee(), 0.001, "Active loan should have 0.0 fee");
    }
}

