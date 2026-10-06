/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Loan;
import com.j256.ormlite.support.ConnectionSource;

/** DAO for Loan entities. */
public final class LoanDao extends BaseDao<Loan> {

    /**
     * Creates a new LoanDao.
     *
     * @param connectionSource the connection source to the database
     */
    public LoanDao(ConnectionSource connectionSource) {
        super(connectionSource, Loan.class);
    }
}
