/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.dao.ReservationDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.model.Reservation;

import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

/** Service handling book reservations and fulfillment. */
public final class ReservationService {

    /** DAO for reservation records. */
    private final ReservationDao reservationDao;

    /** DAO for member records. */
    private final MemberDao memberDao;

    /** DAO for loan records. */
    private final LoanDao loanDao;

    /** Service for book catalog and inventory. */
    private final BookService bookService;

    /** Clock provider for date operations. */
    private final Clock clock;

    /**
     * Creates a new ReservationService.
     *
     * @param reservationDao the reservation DAO
     * @param memberDao the member DAO
     * @param loanDao the loan DAO
     * @param bookService the book service
     * @param clock the clock provider
     */
    public ReservationService(ReservationDao reservationDao, MemberDao memberDao, LoanDao loanDao,
                              BookService bookService, Clock clock) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.loanDao = loanDao;
        this.bookService = bookService;
        this.clock = clock;
    }

    /**
     * Reserves a book for a member.
     *
     * @param bookId the book ID
     * @param memberId the member ID
     * @return the created reservation
     * @throws NotFoundException if the book or member is not found
     */
    public Reservation reserve(int bookId, int memberId) {
        Book book = bookService.findById(bookId);
        if (book == null) {
            throw new NotFoundException("Book not found: " + bookId);
        }
        Member member = memberDao.findById(memberId);
        if (member == null) {
            throw new NotFoundException("Member not found: " + memberId);
        }

        Reservation reservation = new Reservation(member, book, LocalDate.now(clock));
        reservationDao.create(reservation);
        return reservation;
    }

    /**
     * Lists all reservations.
     *
     * @return list of all reservations
     */
    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    /**
     * Fulfills a reservation by updating it, borrowing a copy, and creating a loan in one transaction.
     *
     * @param reservationId the reservation ID to fulfill
     * @return the created loan
     * @throws NotFoundException if the reservation is not found
     * @throws IllegalStateException if the reservation is already fulfilled
     */
    public Loan fulfill(int reservationId) {
        Reservation reservation = reservationDao.findById(reservationId);
        if (reservation == null) {
            throw new NotFoundException("Reservation not found: " + reservationId);
        }
        if (reservation.isFulfilled()) {
            throw new IllegalStateException("Reservation already fulfilled: " + reservationId);
        }

        reservation.setFulfilled(true);
        LocalDate today = LocalDate.now(clock);
        Loan loan = new Loan(reservation.getMember(), reservation.getBook(), today, LoanPolicy.dueDate(today));

        try {
            return reservationDao.transaction(() -> {
                reservationDao.update(reservation);
                bookService.borrow(reservation.getBook().getId());
                loanDao.create(loan);
                return loan;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
