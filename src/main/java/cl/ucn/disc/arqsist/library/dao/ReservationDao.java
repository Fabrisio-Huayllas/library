/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Reservation;
import com.j256.ormlite.support.ConnectionSource;

/** DAO for Reservation entities. */
public final class ReservationDao extends BaseDao<Reservation> {

    /**
     * Creates a new ReservationDao.
     *
     * @param connectionSource the connection source to the database
     */
    public ReservationDao(ConnectionSource connectionSource) {
        super(connectionSource, Reservation.class);
    }
}
