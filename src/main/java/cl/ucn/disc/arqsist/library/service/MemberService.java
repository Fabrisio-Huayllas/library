/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.model.Member;

import java.util.List;

/** Service handling member lifecycle operations. */
public final class MemberService {

    /** DAO for member entities. */
    private final MemberDao memberDao;

    /**
     * Creates a new MemberService.
     *
     * @param memberDao the member DAO
     */
    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    /**
     * Registers a new member.
     *
     * @param member the member to register
     * @return the registered member
     */
    public Member register(Member member) {
        memberDao.create(member);
        return member;
    }

    /**
     * Finds all registered members.
     *
     * @return list of all members
     */
    public List<Member> findAll() {
        return memberDao.findAll();
    }
}
