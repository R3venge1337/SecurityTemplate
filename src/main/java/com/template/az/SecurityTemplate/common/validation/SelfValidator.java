package com.template.az.SecurityTemplate.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class SelfValidator implements ConstraintValidator<SelfValidator.SelfValidation, SelfValidator.SelfValidate> {
  private static final String DEFAULT_MESSAGE = "error.self.validation.failed";

  @Inherited
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Constraint(validatedBy = SelfValidator.class)
  public @interface SelfValidation {
    String message() default DEFAULT_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
  }

  public interface SelfValidate {
    boolean validate(final ConstraintValidatorContext ctx);
  }

  @Override
  public void initialize(final SelfValidation selfValidation) {
  }

  @Override
  public boolean isValid(final SelfValidate selfValidate, final ConstraintValidatorContext context) {
    final boolean valid = selfValidate.validate(context);
    context.disableDefaultConstraintViolation();
    return valid;
  }

  public static void addViolation(final ConstraintValidatorContext context, final String field, final String message) {
    context.buildConstraintViolationWithTemplate(message)
      .addPropertyNode(field)
      .addConstraintViolation();
  }
}