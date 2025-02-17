package com.template.az.SecurityTemplate.auth.dto;

public record JWTBodyAttributes(String userUuid, String accountUuid, String username, String userEmail, String userRole,
                                String accountEnabled, String accountLocked, String passwordChangedTime) {
}
