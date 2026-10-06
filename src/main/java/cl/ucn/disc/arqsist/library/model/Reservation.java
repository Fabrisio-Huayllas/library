/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.model;

import cl.ucn.disc.arqsist.library.db.LocalDatePersister;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;

/** Represents a reservation for a book made by a member. */
@DatabaseTable(tableName = "reservations")
public final class Reservation {

    /** Primary key identifier. */
    @DatabaseField(generatedId = true)
    private int id;

    /** Member who made the reservation. */
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Member member;

    /** Book that was reserved. */
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Book book;

    /** Date when the reservation was created. */
    @DatabaseField(canBeNull = false, persisterClass = LocalDatePersister.class)
    private LocalDate reservedAt;

    /** Whether the reservation has been fulfilled into a loan. */
    @DatabaseField
    private boolean fulfilled;

    /** Default no-arg constructor for ORMLite. */
    public Reservation() {
    }

    /**
     * Creates a new Reservation with the specified member, book, and reservation date.
     *
     * @param member the member making the reservation
     * @param book the book being reserved
     * @param reservedAt the date of the reservation
     */
    public Reservation(Member member, Book book, LocalDate reservedAt) {
        this.member = member;
        this.book = book;
        this.reservedAt = reservedAt;
        this.fulfilled = false;
    }

    /**
     * Returns the reservation identifier.
     *
     * @return the reservation ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the reservation identifier.
     *
     * @param id the reservation ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the member who made this reservation.
     *
     * @return the member
     */
    public Member getMember() {
        return member;
    }

    /**
     * Sets the member who made this reservation.
     *
     * @param member the member
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * Returns the book that was reserved.
     *
     * @return the book
     */
    public Book getBook() {
        return book;
    }

    /**
     * Sets the book that was reserved.
     *
     * @param book the book
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Returns the date when this reservation was made.
     *
     * @return the reservation date
     */
    public LocalDate getReservedAt() {
        return reservedAt;
    }

    /**
     * Sets the date when this reservation was made.
     *
     * @param reservedAt the reservation date
     */
    public void setReservedAt(LocalDate reservedAt) {
        this.reservedAt = reservedAt;
    }

    /**
     * Returns whether the reservation has been fulfilled.
     *
     * @return true if fulfilled, false otherwise
     */
    public boolean isFulfilled() {
        return fulfilled;
    }

    /**
     * Sets whether the reservation has been fulfilled.
     *
     * @param fulfilled true if fulfilled, false otherwise
     */
    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }
}
