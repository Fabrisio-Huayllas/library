/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Reservation;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
public final class ReservationDao extends BaseDao<Reservation> {

    public ReservationDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Reservation.class);
    }
}
