package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.auth.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
class AuthorizationUser implements UserDetails {

    private final AccountDto accountDto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String rolePrefix = "ROLE_";
        return accountDto.roles().stream().map(role -> new SimpleGrantedAuthority(rolePrefix + role)).toList();
    }

    @Override
    public String getPassword() {
        return accountDto.password();
    }

    @Override
    public String getUsername() {
        return accountDto.username();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return accountDto.isActive();
    }
}
