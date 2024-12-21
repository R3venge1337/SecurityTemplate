package com.template.az.SecurityTemplate.auth.dto;

import java.time.LocalDateTime;

public record RoleDto(Long id, String name, String description, LocalDateTime roleCreatedDate) {
}
