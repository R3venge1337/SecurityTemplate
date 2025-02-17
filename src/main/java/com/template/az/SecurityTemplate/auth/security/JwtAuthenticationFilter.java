package com.template.az.SecurityTemplate.auth.security;


import com.template.az.SecurityTemplate.auth.dto.JWTBodyAttributes;
import com.template.az.SecurityTemplate.auth.dto.UserWithAccount;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtTokenUtil;


    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String login;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        login = jwtTokenUtil.getUsernameFromToken(jwt);
        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwt);
            final String username = claims.get(JwtUtils.USER_LOGIN, String.class);
            final String userRole = String.valueOf(claims.get(JwtUtils.USER_TYPE, ArrayList.class));
            final String accountUuid = claims.get(JwtUtils.ACCOUNT_UUID, String.class);
            final String userUuid = claims.get(JwtUtils.USER_UUID, String.class);
            final String userEmail = claims.get(JwtUtils.USER_EMAIL, String.class);
            final String accountEnabled = String.valueOf(claims.get(JwtUtils.ACCOUNT_ENABLED, Boolean.class));
            final String accountLocked = String.valueOf(claims.get(JwtUtils.ACCOUNT_LOCKED, Boolean.class));
            final String passwordChangedTime = String.valueOf(claims.get(JwtUtils.PASSWORD_LAST_CHANGE_TIME, LocalDateTime.class));

            final JWTBodyAttributes attributes = new JWTBodyAttributes(userUuid, accountUuid, username, userEmail, userRole, accountEnabled, accountLocked, passwordChangedTime);


            if (!jwtTokenUtil.isTokenExpired(jwt)) {
                final UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(createJwtBody(attributes), null, List.of(new SimpleGrantedAuthority(userRole)));

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private UserWithAccount createJwtBody(final JWTBodyAttributes attributes) {
        return new UserWithAccount(UUID.fromString(attributes.userUuid()), UUID.fromString(attributes.accountUuid()), attributes.username(), null, attributes.userEmail(), Set.of(attributes.userRole()), Boolean.valueOf(attributes.accountEnabled()), Boolean.valueOf(attributes.accountLocked()), LocalDateTime.parse(attributes.passwordChangedTime()));
    }
}
