/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/** Represents a book in the library catalog. */
@DatabaseTable(tableName = "books")
public final class Book {

    /** Primary key identifier. */
    @DatabaseField(generatedId = true)
    private int id;

    /** Title of the book. */
    @DatabaseField(canBeNull = false)
    private String title;

    /** Author of the book. */
    @DatabaseField(canBeNull = false)
    private String author;

    /** International Standard Book Number. */
    @DatabaseField(canBeNull = false)
    private String isbn;

    /** Total number of copies owned by the library. */
    @DatabaseField(canBeNull = false)
    private int totalCopies;

    /** Number of copies currently available for loan. */
    @DatabaseField(canBeNull = false)
    private int availableCopies;

    /** Default no-arg constructor for ORMLite. */
    public Book() {
    }

    /**
     * Creates a new Book with the given details.
     *
     * @param title the book title
     * @param author the author
     * @param isbn the ISBN
     * @param totalCopies total copies count
     */
    public Book(String title, String author, String isbn, int totalCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    /**
     * Returns the book identifier.
     *
     * @return the book ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the book identifier.
     *
     * @param id the book ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the book title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the book title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the author of the book.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the book.
     *
     * @param author the author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns the ISBN of the book.
     *
     * @return the ISBN
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param isbn the ISBN
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Returns the total copies count.
     *
     * @return total copies
     */
    public int getTotalCopies() {
        return totalCopies;
    }

    /**
     * Sets the total copies count.
     *
     * @param totalCopies total copies
     */
    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    /**
     * Returns the available copies count.
     *
     * @return available copies
     */
    public int getAvailableCopies() {
        return availableCopies;
    }

    /**
     * Sets the available copies count.
     *
     * @param availableCopies available copies
     */
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
}
