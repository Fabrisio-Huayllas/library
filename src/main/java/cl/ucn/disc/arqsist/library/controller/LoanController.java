/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.controller;

import cl.ucn.disc.arqsist.library.service.LoanService;
import cl.ucn.disc.arqsist.library.service.MemberService;
import io.javalin.config.JavalinConfig;

import java.util.Objects;

/** HTTP controller for loan routes. */
public final class LoanController {

    /** Member service. */
    private final MemberService memberService;

    /** Loan service. */
    private final LoanService loanService;

    /**
     * Creates a new LoanController.
     *
     * @param memberService the member service
     * @param loanService the loan service
     */
    public LoanController(MemberService memberService, LoanService loanService) {
        this.memberService = memberService;
        this.loanService = loanService;
    }

    /**
     * Registers loan HTTP routes in Javalin.
     *
     * @param config the Javalin config
     */
    public void register(JavalinConfig config) {
        config.routes.post("/loans", ctx -> {
            int memberId = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("memberId")));
            int bookId = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("bookId")));
            ctx.json(memberService.checkout(memberId, bookId));
        });
        config.routes.get("/loans", ctx -> ctx.json(loanService.findAll()));
        config.routes.post("/loans/{id}/return", ctx -> ctx.json(loanService.returnLoan(Integer.parseInt(ctx.pathParam("id")))));
        config.routes.get("/loans/overdue", ctx -> ctx.json(loanService.overdueLoans()));
    }
}
