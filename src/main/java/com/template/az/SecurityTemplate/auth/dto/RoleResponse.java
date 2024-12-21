package com.template.az.SecurityTemplate.auth.dto;

import java.util.Set;

public record RoleResponse(Long id, String name, String description, Set<PermissionResponse> permissions) {
}
