package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.auth.dto.AccountDto;
import com.template.az.SecurityTemplate.auth.dto.AccountResponse;
import com.template.az.SecurityTemplate.auth.dto.PermissionResponse;
import com.template.az.SecurityTemplate.auth.dto.RoleResponse;
import com.template.az.SecurityTemplate.auth.dto.UserResponse;
import com.template.az.SecurityTemplate.auth.dto.UserWithAccount;

import java.util.Set;
import java.util.stream.Collectors;

class DomainMapper {
    public static UserResponse mapToUserResponse(final User user) {
        return new UserResponse(user.getUuid(), user.getFirstName(), user.getSecondName(), user.getSurname(), user.getYearOfBirth(), mapToAccountResponse(user.getAccount()));
    }

    public static UserWithAccount mapToUserWithAccount(final User user) {
        final Account account = user.getAccount();
        return createUserWithAccount(user, account);
    }

    public static AccountDto mapToDto(final Account account) {
        return new AccountDto(account.getUuid(), account.getUsername(), account.getPassword(), account.getEmail(), account.getEnabled(), account.getAccountNonLocked(), account.getCreatedAt(), account.getPasswordLastTimeChanged(), mapToSetRoles(account));
    }

    private static Set<String> mapToSetRoles(final Account account) {
        return account.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static AccountResponse mapToAccountResponse(final Account account) {
        return new AccountResponse(account.getUuid(), account.getUsername(), account.getEmail(), account.getEnabled(), account.getRoles().stream().map(DomainMapper::mapToRoleResponse).collect(Collectors.toUnmodifiableSet()));
    }

    public static RoleResponse mapToRoleResponse(final Role role) {
        return new RoleResponse(role.getId(), role.getName(), role.getDescription(), role.getPermissions().stream().map(DomainMapper::mapToPermissionResponse).collect(Collectors.toUnmodifiableSet()));
    }

    public static PermissionResponse mapToPermissionResponse(final Permission permission) {
        return new PermissionResponse(permission.getId(), permission.getName(), permission.getDescription());
    }


    public static UserWithAccount createUserWithAccount(final User user, final Account account) {
        return UserWithAccount.builder()
                .userUuid(user.getUuid())
                .accountUuid(account.getUuid())
                .username(user.getSurname())
                .email(account.getEmail())
                .roles(mapToSetRoles(account))
                .isEnabled(account.getEnabled())
                .isNonLocked(account.getAccountNonLocked())
                .passwordLastTimeChanged(account.getPasswordLastTimeChanged())
                .build();
    }
}
