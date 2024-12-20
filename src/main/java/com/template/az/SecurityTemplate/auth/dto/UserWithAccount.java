package com.template.az.SecurityTemplate.auth.dto;

import java.util.Set;
import java.util.UUID;

public record UserWithAccount(UUID userUuid, UUID accountUuid, String username, String password, String email,
                              Set<String> roles, Boolean isEnabled, Boolean isNonLocked) {
}