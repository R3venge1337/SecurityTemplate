package com.template.az.SecurityTemplate.common.exception;

public class PasswordDoesNotMatchException extends RuntimeException {
  public PasswordDoesNotMatchException(final String message) {
    super(message);
  }
}
