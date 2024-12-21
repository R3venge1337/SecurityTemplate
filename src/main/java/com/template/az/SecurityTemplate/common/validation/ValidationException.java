package com.template.az.SecurityTemplate.common.validation;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class ValidationException extends RuntimeException {

  @Getter
  private final Set<ErrorDto> errorDtos;

  public ValidationException(final ErrorDto... errorDtos) {
    this.errorDtos = Stream.of(errorDtos).collect(toSet());
  }

  ValidationException(final ConstraintViolation<?>... violations) {
    errorDtos = Arrays.stream(violations)
      .map(this::mapToDto)
      .collect(toSet());
  }

  private ErrorDto mapToDto(final ConstraintViolation<?> violation) {
    final String property = violation.getPropertyPath().toString();
    final String reason = mapReason(violation.getMessageTemplate());
    return new ErrorDto(property, reason);
  }

  private String mapReason(final String messageTemplate) {
    if (isJavaxValidationMessage(messageTemplate)) {
      return ValidationMessage.nameByCode(messageTemplate);
    } else {
      return messageTemplate;
    }
  }

  //there is no simple way of specifying if violation is from javax.validation or not
  //we tried to find a solution in ProRMS, but without a success
  private static boolean isJavaxValidationMessage(final String messageTemplate) {
    return messageTemplate.startsWith("{") && messageTemplate.endsWith("}");
  }
}