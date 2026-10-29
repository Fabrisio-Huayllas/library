/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.ReservationDao;
import cl.ucn.disc.arqsist.library.db.Database;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Reservation;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Verifies that database seeding operates idempotently and properly initializes state. */
class DatabaseSeedTest {

    @Test
    void databaseSeedIsIdempotentAndInitializesInventoryCorrectly() throws Exception {
        Database db = new Database("jdbc:sqlite::memory:");
        db.seedIfEmpty();

        BookDao bookDao = new BookDao(db.connectionSource());
        LoanDao loanDao = new LoanDao(db.connectionSource());
        ReservationDao reservationDao = new ReservationDao(db.connectionSource());

        List<Loan> loans = loanDao.findAll();
        List<Reservation> reservations = reservationDao.findAll();

        assertFalse(loans.isEmpty(), "Loans should not be empty after seeding");
        assertFalse(reservations.isEmpty(), "Reservations should not be empty after seeding");

        LocalDate today = LocalDate.now();
        long overdueCount = loans.stream()
                .filter(l -> !l.isReturned() && l.getDueDate().isBefore(today))
                .count();
        assertEquals(1, overdueCount, "Exactly one loan should be overdue");

        List<Loan> openLoans = loans.stream().filter(l -> !l.isReturned()).toList();
        for (Loan openLoan : openLoans) {
            Book book = bookDao.findById(openLoan.getBook().getId());
            assertTrue(book.getAvailableCopies() < book.getTotalCopies(),
                    "Books with open loans should have fewer available copies than total copies");
        }

        int initialLoansCount = loans.size();
        int initialReservationsCount = reservations.size();

        db.seedIfEmpty();

        assertEquals(initialLoansCount, loanDao.findAll().size(),
                "Loan count should not change after calling seedIfEmpty again");
        assertEquals(initialReservationsCount, reservationDao.findAll().size(),
                "Reservation count should not change after calling seedIfEmpty again");
    }
}
