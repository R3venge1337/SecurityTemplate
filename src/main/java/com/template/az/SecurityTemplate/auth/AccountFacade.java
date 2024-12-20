package com.template.az.SecurityTemplate.auth;

import com.template.az.SecurityTemplate.auth.dto.AccountDto;
import com.template.az.SecurityTemplate.auth.dto.AccountFilterForm;
import com.template.az.SecurityTemplate.auth.dto.CreateAccountForm;
import com.template.az.SecurityTemplate.auth.dto.UpdateAccountForm;
import com.template.az.SecurityTemplate.common.controller.PageDto;
import com.template.az.SecurityTemplate.common.controller.PageableRequest;
import com.template.az.SecurityTemplate.common.controller.UuidDto;

import java.util.List;
import java.util.UUID;

public interface AccountFacade {

    PageDto<AccountDto> findAllAccounts(final AccountFilterForm filterForm, final PageableRequest pageableRequest);

    UuidDto saveAccount(final CreateAccountForm createForm);

    AccountDto findAccountByName(final String name);

    AccountDto findAccount(final UUID uuid);

    void updateAccount(final UUID uuid, final UpdateAccountForm updateForm);

    List<AccountDto> findExpiredAccounts(final Integer accountExpiredAge);

    void deleteAccount(final UUID uuid);
}
