package com.template.az.SecurityTemplate.common.exception;

import java.util.UUID;

public class AccountLockedException extends RuntimeException {

    public AccountLockedException(String message) {
        super(message);
    }

    public AccountLockedException(final UUID uuid) {
        super(String.format("Entity already exist by uuid: %s", uuid));
    }

    public AccountLockedException(final String message, final Object... args) {
        super(String.format(message, args));
    }
}
