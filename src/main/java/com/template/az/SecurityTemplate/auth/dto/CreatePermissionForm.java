package com.template.az.SecurityTemplate.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePermissionForm(@NotBlank String name, @NotBlank String description) {
}
