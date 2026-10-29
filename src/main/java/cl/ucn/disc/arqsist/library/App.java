/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library;

import cl.ucn.disc.arqsist.library.controller.BookController;
import cl.ucn.disc.arqsist.library.controller.LoanController;
import cl.ucn.disc.arqsist.library.controller.MemberController;
import cl.ucn.disc.arqsist.library.controller.ReservationController;
import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.dao.ReservationDao;
import cl.ucn.disc.arqsist.library.db.Database;
import cl.ucn.disc.arqsist.library.service.BookService;
import cl.ucn.disc.arqsist.library.service.LoanService;
import cl.ucn.disc.arqsist.library.service.MemberService;
import cl.ucn.disc.arqsist.library.service.NotFoundException;
import cl.ucn.disc.arqsist.library.service.ReservationService;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JavalinJackson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.ZoneId;
import java.util.Map;

/** Main entry point for the library application. */
public final class App {

    /** Application logger. */
    private static final Logger log = LoggerFactory.getLogger(App.class);

    /**
     * Bootstraps the application, initialises DAOs, services, controllers, and starts Javalin.
     *
     * @param args command line arguments
     * @throws Exception if application initialization fails
     */
    public static void main(String[] args) throws Exception {
        log.info("Connecting to database");
        Database db = new Database("jdbc:sqlite:database.db");

        log.info("Seeding database if empty");
        db.seedIfEmpty();

        log.info("Initializing DAOs");
        BookDao bookDao = new BookDao(db.connectionSource());
        MemberDao memberDao = new MemberDao(db.connectionSource());
        LoanDao loanDao = new LoanDao(db.connectionSource());
        ReservationDao reservationDao = new ReservationDao(db.connectionSource());

        log.info("Creating clock and services");
        Clock clock = Clock.system(ZoneId.of("America/Santiago"));
        BookService bookService = new BookService(bookDao);
        MemberService memberService = new MemberService(memberDao);
        LoanService loanService = new LoanService(loanDao, memberDao, bookService, clock);
        ReservationService reservationService = new ReservationService(reservationDao, memberDao, loanDao, bookService, clock);

        log.info("Initializing controllers");
        BookController bookController = new BookController(bookService);
        MemberController memberController = new MemberController(memberService);
        LoanController loanController = new LoanController(loanService);
        ReservationController reservationController = new ReservationController(reservationService);

        log.info("Starting Javalin server");
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/public";
                staticFiles.location = Location.CLASSPATH;
            });
            config.jsonMapper(new JavalinJackson().updateMapper(mapper ->
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)));
            config.routes.exception(NotFoundException.class, (e, ctx) ->
                ctx.status(404).json(Map.of("error", e.getMessage())));
            config.routes.exception(IllegalArgumentException.class, (e, ctx) ->
                ctx.status(400).json(Map.of("error", e.getMessage())));
            config.routes.exception(IllegalStateException.class, (e, ctx) ->
                ctx.status(409).json(Map.of("error", e.getMessage())));
            bookController.register(config);
            memberController.register(config);
            loanController.register(config);
            reservationController.register(config);
        });
        app.start(7070);
    }
}
