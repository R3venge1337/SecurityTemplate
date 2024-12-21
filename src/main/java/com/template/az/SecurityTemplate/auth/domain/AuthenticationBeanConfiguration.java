package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.auth.AccountFacade;
import com.template.az.SecurityTemplate.auth.AuthenticationFacade;
import com.template.az.SecurityTemplate.auth.EmailFacade;
import com.template.az.SecurityTemplate.auth.PasswordFacade;
import com.template.az.SecurityTemplate.auth.UserFacade;
import com.template.az.SecurityTemplate.auth.dto.AccountDto;
import com.template.az.SecurityTemplate.auth.security.JwtAuthenticationFilter;
import com.template.az.SecurityTemplate.auth.security.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class AuthenticationBeanConfiguration {

    @Bean
    UserDetailsService userDetailsService(final AccountFacade accountFacade) {
        return username -> {
            final AccountDto dto = accountFacade.findAccountByName(username);
            return new AuthorizationUser(dto);
        };
    }

    @Bean
    AuthenticationManager authenticationManager(final AccountFacade accountFacade) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService(accountFacade));
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtUtils jwtUtils() {
        return new JwtUtils();
    }

    @Bean
    AuthenticationFacade authenticationFacade(final AccountRepository accountRepository, final JwtUtils jwtUtils, final AuthenticationManager authenticationManager,
                                              final EmailFacade emailService, final UserFacade userFacade, final PermissionRepository permissionRepository,
                                              final RoleRepository roleRepository, final PasswordService passwordService, final UserRepository userRepository) {
        return new AuthenticationService(authenticationManager, userRepository, accountRepository, roleRepository, passwordService, permissionRepository, jwtUtils, userFacade, emailService);
    }

    @Bean
    PasswordFacade passwordFacade(final PasswordEncoder passwordEncoder) {
        return new PasswordService(passwordEncoder);
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils());
    }
}
