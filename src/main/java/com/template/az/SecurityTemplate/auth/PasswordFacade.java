package com.template.az.SecurityTemplate.auth;

public interface PasswordFacade {

    String encodePassword(final String password);

    Boolean matchPassword(final String raw, final String encoded);
}
