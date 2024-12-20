package com.template.az.SecurityTemplate.common.exception;

import java.util.UUID;

public class AlreadyVerifiedException extends RuntimeException {
    public AlreadyVerifiedException(final String message) {
        super(message);
    }

    public AlreadyVerifiedException(final UUID uuid) {
        super(String.format("Account already has been verified: %s", uuid));
    }

    public AlreadyVerifiedException(final String message, final Object... args) {
        super(String.format(message, args));
    }

}
