/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.model.Book;

import java.util.List;

/** Service handling book catalog operations and inventory management. */
public final class BookService {

    /** DAO for accessing book entities. */
    private final BookDao dao;

    /**
     * Creates a new BookService.
     *
     * @param dao the book DAO
     */
    public BookService(BookDao dao) {
        this.dao = dao;
    }

    /**
     * Lists all books in the catalog.
     *
     * @return list of all books
     */
    public List<Book> listAll() {
        return dao.findAll();
    }

    /**
     * Finds a book by its identifier.
     *
     * @param id the book ID
     * @return the book if found, or null otherwise
     */
    public Book findById(int id) {
        return dao.findById(id);
    }

    /**
     * Creates a new book and initializes its available copies.
     *
     * @param book the book to create
     * @return the created book
     */
    public Book create(Book book) {
        book.setAvailableCopies(book.getTotalCopies());
        dao.create(book);
        return book;
    }

    /**
     * Decrements the available copy count of a book.
     *
     * @param bookId the ID of the book to borrow
     * @throws NotFoundException if the book does not exist
     * @throws IllegalStateException if no copies are available
     */
    public void borrow(int bookId) {
        Book book = dao.findById(bookId);
        if (book == null) {
            throw new NotFoundException("Book not found: " + bookId);
        }
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies of book " + bookId);
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        dao.update(book);
    }

    /**
     * Increments the available copy count of a returned book.
     *
     * @param bookId the ID of the book to return
     * @throws NotFoundException if the book does not exist
     */
    public void returnCopy(int bookId) {
        Book book = dao.findById(bookId);
        if (book == null) {
            throw new NotFoundException("Book not found: " + bookId);
        }
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        dao.update(book);
    }
}
