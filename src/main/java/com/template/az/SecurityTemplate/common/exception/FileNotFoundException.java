package com.template.az.SecurityTemplate.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class FileNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(final UUID uuid) {
        super(String.format("No entity with UUID: %s", uuid));
    }

    public FileNotFoundException(final String message, final Object... args) {
        super(String.format(message, args));
    }
}
