package com.template.az.SecurityTemplate.common.exception;

import java.util.UUID;

public class PasswordExpiredException extends RuntimeException {

    public PasswordExpiredException(String message) {
        super(message);
    }

    public PasswordExpiredException(final UUID uuid) {
        super(String.format("Entity already exist by uuid: %s", uuid));
    }

    public PasswordExpiredException(final String message, final Object... args) {
        super(String.format(message, args));
    }
}
