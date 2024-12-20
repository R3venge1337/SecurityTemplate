package com.template.az.SecurityTemplate.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePermissionForm(@NotBlank String name) {
}
