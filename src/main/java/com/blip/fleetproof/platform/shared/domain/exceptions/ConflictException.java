package com.blip.fleetproof.platform.shared.domain.exceptions;

/**
 * Raised when a request is well formed but conflicts with the current state of the domain
 * (duplicate resource, illegal state transition). Mapped to HTTP 409 by the REST layer.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
