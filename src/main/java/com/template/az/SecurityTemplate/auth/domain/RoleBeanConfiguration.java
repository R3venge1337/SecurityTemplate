package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.auth.RoleFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RoleBeanConfiguration {
    @Bean
    RoleFacade roleFacade(final RoleRepository roleRepository) {
        return new RoleService(roleRepository);
    }
}
