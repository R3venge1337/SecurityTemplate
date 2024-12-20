package com.template.az.SecurityTemplate.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAccountForm(@NotBlank String name, @NotBlank String password, @NotBlank @Email String email,
                                @NotNull Boolean active) {
}
