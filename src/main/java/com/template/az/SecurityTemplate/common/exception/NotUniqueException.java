package com.template.az.SecurityTemplate.common.exception;

import lombok.Getter;

public class NotUniqueException extends RuntimeException {
  @Getter
  private final String field;

  public NotUniqueException(final String field, final String message) {
    super(message);
    this.field = field;
  }
}