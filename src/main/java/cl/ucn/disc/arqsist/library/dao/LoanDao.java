/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Loan;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
public final class LoanDao extends BaseDao<Loan> {

    public LoanDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Loan.class);
    }
}
