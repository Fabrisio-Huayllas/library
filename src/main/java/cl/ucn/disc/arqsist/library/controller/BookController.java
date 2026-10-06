/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.controller;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.service.BookService;
import io.javalin.config.JavalinConfig;

/** HTTP controller for book routes. */
public final class BookController {

    /** Book service. */
    private final BookService service;

    /** Book DAO. */
    private final BookDao dao;

    /**
     * Creates a new BookController.
     *
     * @param service the book service
     * @param dao the book DAO
     */
    public BookController(BookService service, BookDao dao) {
        this.service = service;
        this.dao = dao;
    }

    /**
     * Registers book HTTP routes in Javalin.
     *
     * @param config the Javalin config
     */
    public void register(JavalinConfig config) {
        config.routes.get("/books", ctx -> ctx.json(dao.findAll()));
        config.routes.get("/books/{id}", ctx -> ctx.json(service.findById(Integer.parseInt(ctx.pathParam("id")))));
        config.routes.post("/books", ctx -> ctx.json(service.create(ctx.bodyAsClass(Book.class))));
    }
}
