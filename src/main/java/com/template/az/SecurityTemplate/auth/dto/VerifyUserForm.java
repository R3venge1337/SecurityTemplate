package com.template.az.SecurityTemplate.auth.dto;

public record VerifyUserForm(String email, String verificationCode) {
}
