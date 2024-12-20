package com.template.az.SecurityTemplate.auth.dto;

import java.time.LocalDateTime;

public record AccountFilterForm(String nickname, String email, LocalDateTime createdDate, Boolean isActive, String role) {
}
