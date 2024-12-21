package com.template.az.SecurityTemplate.auth.dto;

import java.time.LocalDateTime;

public record PermissionDto(Long id, String name, LocalDateTime roleCreatedDate) {
}
