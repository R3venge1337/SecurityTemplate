package com.template.az.SecurityTemplate.auth;

import jakarta.mail.MessagingException;

public interface EmailFacade {

    void sendVerificationEmail(final String to, final String subject, final String text) throws MessagingException;
}
