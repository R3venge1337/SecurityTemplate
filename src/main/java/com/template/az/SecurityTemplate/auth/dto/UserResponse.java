package com.template.az.SecurityTemplate.auth.dto;

import java.util.UUID;

public record UserResponse(UUID uuid, String name, String secondName, String surname, Short yearOfBirth,
                           AccountResponse account) {
}
