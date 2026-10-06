/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Member;
import com.j256.ormlite.support.ConnectionSource;

/** DAO for Member entities. */
public final class MemberDao extends BaseDao<Member> {

    /**
     * Creates a new MemberDao.
     *
     * @param connectionSource the connection source to the database
     */
    public MemberDao(ConnectionSource connectionSource) {
        super(connectionSource, Member.class);
    }
}
