package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.auth.AccountFacade;
import com.template.az.SecurityTemplate.auth.PasswordFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AccountBeanConfiguration {

    @Bean
    AccountFacade accountFacade(final AccountRepository accountRepository, final RoleRepository accountRoleRepository, final PasswordFacade passwordFacade) {
        return new AccountService(accountRepository, accountRoleRepository, passwordFacade);
    }
}
