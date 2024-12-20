package com.template.az.SecurityTemplate.auth.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record AccountDto(UUID uuid, String username, String password, String email, Boolean isActive,
                         LocalDateTime createdDate, Set<String> roles) {
}
