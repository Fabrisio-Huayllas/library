/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.service;

import java.time.LocalDate;

/** Central policy for loan due dates and overdue fees. */
public final class LoanPolicy {

    /** Number of days allowed for a loan. */
    public static final int DUE_DAYS = 21;

    /** Fee charged for each overdue day. */
    public static final double FEE_PER_DAY = 1.0;

    private LoanPolicy() {
    }

    /** Calculates the due date for a loan.
     * @param loanDate loan start date
     * @return due date
     */
    public static LocalDate dueDate(LocalDate loanDate) {
        return loanDate.plusDays(DUE_DAYS);
    }
}