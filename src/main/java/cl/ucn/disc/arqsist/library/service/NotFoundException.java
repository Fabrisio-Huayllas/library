/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.service;

/** Signals that a requested domain entity does not exist. */
public final class NotFoundException extends RuntimeException {

    /** Creates an exception with a descriptive message.
     * @param message error message
     */
    public NotFoundException(String message) {
        super(message);
    }
}