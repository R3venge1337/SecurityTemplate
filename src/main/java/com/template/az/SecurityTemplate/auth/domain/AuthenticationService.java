package com.template.az.SecurityTemplate.auth.domain;


import com.template.az.SecurityTemplate.auth.AuthenticationFacade;
import com.template.az.SecurityTemplate.auth.EmailFacade;
import com.template.az.SecurityTemplate.auth.UserFacade;
import com.template.az.SecurityTemplate.auth.dto.AuthenticationRequest;
import com.template.az.SecurityTemplate.auth.dto.AuthenticationResponse;
import com.template.az.SecurityTemplate.auth.dto.CreateUserForm;
import com.template.az.SecurityTemplate.auth.dto.UserResponse;
import com.template.az.SecurityTemplate.auth.dto.UserWithAccount;
import com.template.az.SecurityTemplate.auth.security.JwtUtils;
import com.template.az.SecurityTemplate.common.exception.AccountLockedException;
import com.template.az.SecurityTemplate.common.exception.AlreadyExistException;
import com.template.az.SecurityTemplate.common.exception.AlreadyVerifiedException;
import com.template.az.SecurityTemplate.common.exception.NotFoundException;
import com.template.az.SecurityTemplate.common.exception.PasswordDoesNotMatchException;
import com.template.az.SecurityTemplate.common.exception.PasswordExpiredException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static com.template.az.SecurityTemplate.auth.domain.DomainMapper.mapToUserResponse;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.ACCOUNT_LOCKED;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.ACCOUNT_NOT_FOUND;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.ACCOUNT_NOT_VERIFIED;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.ACCOUNT_VERIFIED;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.EMAIL_IS_TAKEN;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.LOGIN_FIELDS_EMPTY;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.PASSWORD_EXPIRED_AFTER_POLICY;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.PASSWORD_NOT_MATCH;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.REGISTER_FORM_EMPTY;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.ROLE_NOT_FOUND;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.USER_IS_TAKEN;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.USER_NOT_FOUND;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.VERIFICATION_CODE_INVALID;


@RequiredArgsConstructor
class AuthenticationService implements AuthenticationFacade {

    private static final String DEFAULT_ROLE = "USER";
    private static final Set<String> DEFAULT_PERMISSIONS_FOR_USER = Set.of("READ", "WRITE");

    @Value(value = "${ACCOUNT_LOCK_TIME}")
    private Long ACCOUNT_LOCK_TIME;

    @Value(value = "${VERIFICATION_CODE_EXPIRED_TIME}")
    private Long VERIFICATION_CODE_EXPIRED_TIME;

    @Value(value = "${RESEND_VERIFICATION_CODE_EXPIRED_TIME}")
    private Long RESEND_VERIFICATION_CODE_EXPIRED_TIME;

    @Value(value = "${MAX_FAILED_ATTEMPTS}")
    private Long MAX_FAILED_ATTEMPTS;

    @Value(value = "${PASSWORD_EXPIRATION_DAYS_POLICY}")
    private Long PASSWORD_EXPIRATION_DAYS_POLICY;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordService passwordService;
    private final PermissionRepository permissionRepository;
    private final JwtUtils jwtTokenUtil;
    private final UserFacade userFacade;
    private final EmailFacade emailService;

    public AuthenticationResponse authenticateUser(@Valid @RequestBody final AuthenticationRequest loginRequest) {
        Validate.notNull(loginRequest, LOGIN_FIELDS_EMPTY);
        UserWithAccount user = userFacade.findByUsername(loginRequest.login());
        Authentication auth;
        String jwt = null;

        if (!user.isEnabled()) {
            throw new AlreadyVerifiedException(ACCOUNT_NOT_VERIFIED);
        }

        if (user.passwordLastTimeChanged().plusDays(PASSWORD_EXPIRATION_DAYS_POLICY).isBefore(LocalDateTime.now())) {
            throw new PasswordExpiredException(PASSWORD_EXPIRED_AFTER_POLICY);
        }

        Account account = accountRepository.findByUuid(user.accountUuid())
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND, user.accountUuid()));


        if (!user.isNonLocked() && LocalDateTime.now().isBefore(account.getLockTime())) {
            throw new AccountLockedException(ACCOUNT_LOCKED, MAX_FAILED_ATTEMPTS, account.getLockTime());
        }

        if (!passwordService.matchPassword(loginRequest.password(), account.getPassword())) {
            handleFailedLoginAttempt(account);
            throw new PasswordDoesNotMatchException(PASSWORD_NOT_MATCH);
        }

        try {
            account.setFailedAttempt(0);
            account.setAccountNonLocked(true);
            account.setLockTime(null);
            accountRepository.save(account);
            auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.password()));

            jwt = jwtTokenUtil.generateToken(user);
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (AuthenticationException e) {
            handleFailedLoginAttempt(account);
        }
        return new AuthenticationResponse(jwt);
    }

    @Transactional
    public UserResponse registerUser(@Valid @RequestBody final CreateUserForm createForm) {
        Validate.notNull(createForm, REGISTER_FORM_EMPTY);

        if (accountRepository.existsByUsername(createForm.username())) {
            throw new AlreadyExistException(USER_IS_TAKEN, createForm.username());
        }

        if (accountRepository.existsByEmail(createForm.email())) {
            throw new AlreadyExistException(EMAIL_IS_TAKEN, createForm.email());
        }

        Role userRole = roleRepository.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new NotFoundException(ROLE_NOT_FOUND, DEFAULT_ROLE));

        Set<Permission> permissions = permissionRepository.findRolesByNames(DEFAULT_PERMISSIONS_FOR_USER);

        permissions.forEach(userRole::addPermission);

        Account accountEntity = accountRepository.save(createAccount(createForm, userRole));

        sendVerificationEmail(accountEntity);
        return mapToUserResponse(userRepository.save(createUser(createForm, accountEntity)));
    }

    public void verifyUser(final String verificationCode) {
        Optional<Account> optionalUser = accountRepository.findByVerificationCode(verificationCode);
        if (optionalUser.isPresent()) {
            Account account = optionalUser.get();
            if (account.getVerificationCodeExpiredAt().isBefore(LocalDateTime.now())) {
                throw new AlreadyVerifiedException(VERIFICATION_CODE_INVALID, verificationCode);
            }
            if (account.getVerificationCode().equals(verificationCode)) {
                account.setEnabled(true);
                account.setVerificationCode(null);
                account.setVerificationCodeExpiredAt(null);
                accountRepository.save(account);
            } else {
                throw new AlreadyVerifiedException(VERIFICATION_CODE_INVALID, verificationCode);
            }
        } else {
            throw new NotFoundException(ACCOUNT_NOT_FOUND, verificationCode);
        }
    }

    public void resendVerificationCode(final String email) {
        Optional<Account> optionalUser = accountRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            Account account = optionalUser.get();
            if (account.getEnabled()) {
                throw new AlreadyVerifiedException(ACCOUNT_VERIFIED, account.getUsername());
            }
            account.setVerificationCode(generateVerificationCode());
            account.setVerificationCodeExpiredAt(addHoursToCurrentDateTime(RESEND_VERIFICATION_CODE_EXPIRED_TIME));
            sendVerificationEmail(account);
            accountRepository.save(account);
        } else {
            throw new NotFoundException(USER_NOT_FOUND, email);
        }
    }

    private void sendVerificationEmail(final Account account) {
        String subject = "Account Verification";
        String confirmationUrl = "http://localhost:8080/api/auth/verify-email?token=" + account.getVerificationCode();
        String htmlMessage = """
                 <html>
                      <body style="font-family: Arial, sans-serif";>
                           <div style="background-color: #f5f5f5; padding: 20px;">
                                 <h2 style="color: #333;">Welcome to our app!</h2>
                                 <p style="font-size: 16px;">Please click into link to activate account:</p>
                                <div style="background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                                     <h3 style="color: #333;">Verification Link:</h3>
                                     <p style="font-size: 18px; font-weight: bold; color: #007bff";> %s  </p>
                                </div>
                           </div>
                     </body>
                 </html>
                
                """.formatted(confirmationUrl);

        try {
            emailService.sendVerificationEmail(account.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    private User createUser(final CreateUserForm createForm, final Account entity) {
        User user = new User();
        user.setFirstName(createForm.firstName());
        user.setSecondName(createForm.secondName());
        user.setSurname(createForm.surname());
        user.setTelephone(createForm.phoneNumber());
        user.setYearOfBirth(createForm.yearOfBirth());
        user.setAccount(entity);
        return user;
    }

    private Account createAccount(final CreateUserForm createForm, final Role userRole) {
        Account account = new Account();
        account.setUsername(createForm.username());
        account.setEmail(createForm.email());
        account.setPassword(passwordService.encodePassword(createForm.password()));
        account.setEnabled(false);
        account.setAccountNonLocked(true);
        account.addRole(userRole);
        account.setVerificationCode(generateVerificationCode());
        account.setVerificationCodeExpiredAt(addMinutesToCurrentDateTime(VERIFICATION_CODE_EXPIRED_TIME));
        account.setPasswordLastTimeChanged(LocalDateTime.now());
        return account;
    }

    private void handleFailedLoginAttempt(final Account account) {
        int failedAttempts = account.getFailedAttempt();
        failedAttempts++;

        if (failedAttempts > 3) {
            account.setAccountNonLocked(false);// Lock the account
            account.setLockTime(addMinutesToCurrentDateTime(ACCOUNT_LOCK_TIME));
            accountRepository.save(account);
            throw new AccountLockedException(ACCOUNT_LOCKED);
        }
        account.setFailedAttempt(failedAttempts);
        accountRepository.save(account);
    }

    private static LocalDateTime addMinutesToCurrentDateTime(final Long min) {
        return LocalDateTime.now().plusMinutes(min);
    }

    private LocalDateTime addHoursToCurrentDateTime(final Long hours) {
        return LocalDateTime.now().plusHours(hours);
    }
}

