package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.auth.PermissionFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PermissionBeanConfiguration {
    @Bean
    PermissionFacade permissionFacade(final PermissionRepository permissionRepository) {
        return new PermissionService(permissionRepository);
    }
}
