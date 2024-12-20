package com.template.az.SecurityTemplate.auth.dto;

public record CreateUserForm(String firstName, String secondName, String surname, Short yearOfBirth, String phoneNumber,
                             String username, String password, String email) {
}
