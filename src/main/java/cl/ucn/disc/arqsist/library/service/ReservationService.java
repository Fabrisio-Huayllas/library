/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.dao.ReservationDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.model.Reservation;

import java.time.LocalDate;
import java.util.List;

/** Service handling book reservations and fulfillment. */
public final class ReservationService {

    /** DAO for reservation records. */
    private final ReservationDao reservationDao;

    /** DAO for book inventory. */
    private final BookDao bookDao;

    /** DAO for member records. */
    private final MemberDao memberDao;

    /** DAO for loan records. */
    private final LoanDao loanDao;

    /**
     * Creates a new ReservationService.
     *
     * @param reservationDao the reservation DAO
     * @param bookDao the book DAO
     * @param memberDao the member DAO
     * @param loanDao the loan DAO
     */
    public ReservationService(ReservationDao reservationDao, BookDao bookDao, MemberDao memberDao, LoanDao loanDao) {
        this.reservationDao = reservationDao;
        this.bookDao = bookDao;
        this.memberDao = memberDao;
        this.loanDao = loanDao;
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
        Book book = bookDao.findById(bookId);
        Member member = memberDao.findById(memberId);
        if (book == null) {
            throw new NotFoundException("Book not found: " + bookId);
        }
        if (member == null) {
            throw new NotFoundException("Member not found: " + memberId);
        }
        Reservation reservation = new Reservation(member, book, LocalDate.now());
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
     * Fulfills a reservation by creating a loan with the policy due date.
     *
     * @param reservationId the reservation ID to fulfill
     * @return the created loan
     * @throws IllegalStateException if the reservation is null or already fulfilled
     */
    public Loan fulfill(int reservationId) {
        Reservation reservation = reservationDao.findById(reservationId);
        if (reservation == null || reservation.isFulfilled()) {
            throw new IllegalStateException("Reservation not available");
        }

        reservation.setFulfilled(true);
        reservationDao.update(reservation);

        LocalDate today = LocalDate.now();
        Loan loan = new Loan(reservation.getMember(), reservation.getBook(), today, LoanPolicy.dueDate(today));
        loanDao.create(loan);
        return loan;
    }
}
