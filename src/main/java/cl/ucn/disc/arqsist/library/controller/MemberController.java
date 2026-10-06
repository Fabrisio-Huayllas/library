/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.controller;

import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.service.MemberService;
import io.javalin.config.JavalinConfig;

/** HTTP controller for member routes. */
public final class MemberController {

    /** Member service. */
    private final MemberService service;

    /**
     * Creates a new MemberController.
     *
     * @param service the member service
     */
    public MemberController(MemberService service) {
        this.service = service;
    }

    /**
     * Registers member HTTP routes in Javalin.
     *
     * @param config the Javalin config
     */
    public void register(JavalinConfig config) {
        config.routes.get("/members", ctx -> ctx.json(service.findAll()));
        config.routes.post("/members", ctx -> ctx.json(service.register(ctx.bodyAsClass(Member.class))));
    }
}
