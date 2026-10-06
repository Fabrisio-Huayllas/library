/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.model;

import cl.ucn.disc.arqsist.library.db.LocalDatePersister;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;

/** Represents a loan of a book to a member. */
@DatabaseTable(tableName = "loans")
public final class Loan {

    /** Primary key identifier. */
    @DatabaseField(generatedId = true)
    private int id;

    /** Member who borrowed the book. */
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Member member;

    /** Book that was borrowed. */
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Book book;

    /** Date when the loan was created. */
    @DatabaseField(canBeNull = false, persisterClass = LocalDatePersister.class)
    private LocalDate loanDate;

    /** Due date for returning the book. */
    @DatabaseField(canBeNull = false, persisterClass = LocalDatePersister.class)
    private LocalDate dueDate;

    /** Date when the book was actually returned. */
    @DatabaseField(persisterClass = LocalDatePersister.class)
    private LocalDate returnDate;

    /** Whether the loan has been returned. */
    @DatabaseField
    private boolean returned;

    /** Overdue fee charged for late return. */
    @DatabaseField
    private double overdueFee;

    /** Default no-arg constructor for ORMLite. */
    public Loan() {
    }

    /**
     * Creates a new Loan with the specified member, book, start date, and due date.
     *
     * @param member the member borrowing the book
     * @param book the book being borrowed
     * @param loanDate the date of the loan
     * @param dueDate the due date of the loan
     */
    public Loan(Member member, Book book, LocalDate loanDate, LocalDate dueDate) {
        this.member = member;
        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returned = false;
        this.overdueFee = 0.0;
    }

    /**
     * Returns the loan identifier.
     *
     * @return the loan ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the loan identifier.
     *
     * @param id the loan ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the member associated with this loan.
     *
     * @return the member
     */
    public Member getMember() {
        return member;
    }

    /**
     * Sets the member associated with this loan.
     *
     * @param member the member
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * Returns the book associated with this loan.
     *
     * @return the book
     */
    public Book getBook() {
        return book;
    }

    /**
     * Sets the book associated with this loan.
     *
     * @param book the book
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Returns the start date of the loan.
     *
     * @return the loan date
     */
    public LocalDate getLoanDate() {
        return loanDate;
    }

    /**
     * Sets the start date of the loan.
     *
     * @param loanDate the loan date
     */
    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    /**
     * Returns the due date of the loan.
     *
     * @return the due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Sets the due date of the loan.
     *
     * @param dueDate the due date
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Returns the return date of the loan.
     *
     * @return the return date
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * Sets the return date of the loan.
     *
     * @param returnDate the return date
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Returns whether the loan has been returned.
     *
     * @return true if returned, false otherwise
     */
    public boolean isReturned() {
        return returned;
    }

    /**
     * Sets whether the loan has been returned.
     *
     * @param returned true if returned, false otherwise
     */
    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    /**
     * Returns the overdue fee.
     *
     * @return the overdue fee
     */
    public double getOverdueFee() {
        return overdueFee;
    }

    /**
     * Sets the overdue fee.
     *
     * @param overdueFee the overdue fee
     */
    public void setOverdueFee(double overdueFee) {
        this.overdueFee = overdueFee;
    }
}
