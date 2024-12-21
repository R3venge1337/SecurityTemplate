package com.template.az.SecurityTemplate.common.exception;

public class DateException extends RuntimeException {
  public DateException(final String message) {
    super(message);
  }

  public DateException(final String message, final Object... args) {
    super(String.format(message, args));
  }
}