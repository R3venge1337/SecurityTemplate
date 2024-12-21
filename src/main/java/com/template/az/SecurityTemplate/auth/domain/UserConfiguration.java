package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.auth.UserFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserConfiguration {

    @Bean
    UserFacade userFacade(final UserRepository userRepository, final RoleRepository roleRepository) {
        return new UserService(userRepository, roleRepository);
    }
}
