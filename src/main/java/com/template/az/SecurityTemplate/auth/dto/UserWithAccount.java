package com.template.az.SecurityTemplate.auth.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
public record UserWithAccount(UUID userUuid, UUID accountUuid, String username, String password, String email,
                              Set<String> roles, Boolean isEnabled, Boolean isNonLocked,
                              LocalDateTime passwordLastTimeChanged) {
}
