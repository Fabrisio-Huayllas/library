/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.db;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.dao.ReservationDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.model.Reservation;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/** Owns the database connection, schema creation, and initial data. */
public final class Database {

    private static final Logger log = LoggerFactory.getLogger(Database.class);
    private final ConnectionSource connectionSource;

    /** Creates the database and its tables.
     * @param jdbcUrl JDBC connection URL
     * @throws SQLException if the connection or schema cannot be created
     */
    public Database(String jdbcUrl) throws SQLException {
        this.connectionSource = new JdbcConnectionSource(jdbcUrl);
        TableUtils.createTableIfNotExists(connectionSource, Book.class);
        TableUtils.createTableIfNotExists(connectionSource, Member.class);
        TableUtils.createTableIfNotExists(connectionSource, Loan.class);
        TableUtils.createTableIfNotExists(connectionSource, Reservation.class);
    }

    /** Returns the database connection source.
     * @return connection source
     */
    public ConnectionSource connectionSource() {
        return connectionSource;
    }

    /** Seeds an empty database without duplicating existing data.
     * @throws SQLException if the seed cannot be written
     */
    public void seedIfEmpty() throws SQLException {
        BookDao bookDao = new BookDao(connectionSource);
        MemberDao memberDao = new MemberDao(connectionSource);
        LoanDao loanDao = new LoanDao(connectionSource);
        ReservationDao reservationDao = new ReservationDao(connectionSource);

        if (bookDao.findAll().isEmpty()) {
            log.debug("Seeding books");
            bookDao.create(new Book("Clean Code", "Robert C. Martin", "9780132350884", 3));
            bookDao.create(new Book("The Pragmatic Programmer", "Hunt & Thomas", "9780201616224", 2));
            bookDao.create(new Book("Design Patterns", "Gamma et al.", "9780201633610", 4));
        }
        if (memberDao.findAll().isEmpty()) {
            log.debug("Seeding members");
            memberDao.create(new Member("Ada Lovelace", "ada@example.com"));
            memberDao.create(new Member("Grace Hopper", "grace@example.com"));
            memberDao.create(new Member("Alan Turing", "alan@example.com"));
        }

        List<Book> books = bookDao.findAll();
        List<Member> members = memberDao.findAll();
        LocalDate today = LocalDate.now();

        if (loanDao.findAll().isEmpty()) {
            log.debug("Seeding loans");
            Loan returned = new Loan(members.get(0), books.get(0), today.minusDays(30), today.minusDays(9));
            returned.setReturned(true);
            returned.setReturnDate(today.minusDays(10));
            loanDao.create(returned);

            Loan active = new Loan(members.get(1), books.get(2), today.minusDays(2), today.plusDays(19));
            loanDao.create(active);
            books.get(2).setAvailableCopies(books.get(2).getAvailableCopies() - 1);
            bookDao.update(books.get(2));

            Loan overdue = new Loan(members.get(2), books.get(1), today.minusDays(30), today.minusDays(9));
            loanDao.create(overdue);
            books.get(1).setAvailableCopies(books.get(1).getAvailableCopies() - 1);
            bookDao.update(books.get(1));
        }

        if (reservationDao.findAll().isEmpty()) {
            log.debug("Seeding reservation");
            reservationDao.create(new Reservation(members.get(0), books.get(0), today.minusDays(1)));
        }
    }
}
