package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.auth.AccountFacade;
import com.template.az.SecurityTemplate.auth.PasswordFacade;
import com.template.az.SecurityTemplate.auth.dto.AccountDto;
import com.template.az.SecurityTemplate.auth.dto.AccountFilterForm;
import com.template.az.SecurityTemplate.auth.dto.CreateAccountForm;
import com.template.az.SecurityTemplate.auth.dto.UpdateAccountForm;
import com.template.az.SecurityTemplate.common.controller.PageDto;
import com.template.az.SecurityTemplate.common.controller.PageableRequest;
import com.template.az.SecurityTemplate.common.controller.PageableUtils;
import com.template.az.SecurityTemplate.common.controller.UuidDto;
import com.template.az.SecurityTemplate.common.exception.AlreadyExistException;
import com.template.az.SecurityTemplate.common.exception.NotFoundException;
import com.template.az.SecurityTemplate.common.exception.NotUniqueException;
import com.template.az.SecurityTemplate.common.validation.DtoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.ACCOUNT_IS_TAKEN;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.ACCOUNT_NOT_FOUND;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.EMAIL_IS_TAKEN;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.ROLE_NOT_FOUND;

@RequiredArgsConstructor
class AccountService implements AccountFacade {

    private static final String DEFAULT_ROLE = "USER";
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordFacade passwordFacade;

    @Override
    public PageDto<AccountDto> findAllAccounts(final AccountFilterForm filterForm, final PageableRequest pageableRequest) {
        DtoValidator.validate(filterForm);
        DtoValidator.validate(pageableRequest);

        final AccountSpecification specification = new AccountSpecification(filterForm);
        final Page<AccountDto> accounts = accountRepository.findAll(specification, PageableUtils.createPageable(pageableRequest))
                .map(DomainMapper::mapToDto);

        return PageableUtils.toDto(accounts);
    }

    @Override
    @Transactional
    public UuidDto saveAccount(final CreateAccountForm createForm) {
        DtoValidator.validate(createForm);
        checkUnique(createForm.name());

        if (accountRepository.existsByEmail(createForm.email())) {
            throw new AlreadyExistException(EMAIL_IS_TAKEN, createForm.email());
        }

        final Role accountRole = roleRepository.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new NotFoundException(ROLE_NOT_FOUND, createForm.name()));

        final Account account = new Account();
        account.setUsername(createForm.name());
        account.setEmail(createForm.email());
        account.setEnabled(createForm.active());
        account.setPassword(passwordFacade.encodePassword(createForm.password()));

        account.addRole(accountRole);

        return new UuidDto(accountRepository.save(account).getUuid());
    }

    @Override
    @Transactional
    public AccountDto findAccountByName(final String name) {
        return accountRepository.findByUsername(name)
                .map(DomainMapper::mapToDto)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND, name));
    }

    @Override
    public AccountDto findAccount(final UUID uuid) {
        return accountRepository.findByUuid(uuid)
                .map(DomainMapper::mapToDto)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND, uuid));
    }

    @Override
    @Transactional
    public void updateAccount(final UUID uuid, final UpdateAccountForm updateForm) {
        DtoValidator.validate(updateForm);

        final Account account = accountRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND, uuid));

        final Role accountRole = roleRepository.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new NotFoundException(ROLE_NOT_FOUND, updateForm.name()));

        checkUnique(updateForm.name(), account.getUsername());

        account.setUsername(updateForm.name());
        account.setPassword(passwordFacade.encodePassword(updateForm.password()));
        account.setEmail(updateForm.email());
        account.setEnabled(updateForm.active());

        account.addRole(accountRole);
    }

    @Override
    public List<AccountDto> findExpiredAccounts(final Integer accountExpiredAge) {
        return accountRepository.findExpiredAccounts(accountExpiredAge).stream()
                .map(DomainMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteAccount(final UUID uuid) {
        accountRepository.deleteByUuid(uuid);
    }


    private void checkUnique(final String formLogin, final String entityLogin) {
        if (!formLogin.equals(entityLogin)) {
            checkUnique(formLogin);
        }
    }

    void checkUnique(final String login) {
        if (accountRepository.existsByUsername(login)) {
            throw new NotUniqueException(Account.Fields.username, ACCOUNT_IS_TAKEN);
        }
    }
}
