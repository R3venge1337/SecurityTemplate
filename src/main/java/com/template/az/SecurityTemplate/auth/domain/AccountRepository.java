package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.common.repository.UUIDAwareJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

interface AccountRepository extends UUIDAwareJpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    Boolean existsByUsername(final String name);

    Boolean existsByEmail(final String email);

    Optional<Account> findByUsername(final String username);

    Optional<Account> findByVerificationCode(final String verificationCode);

    Optional<Account> findByEmail(final String email);

    @Query("SELECT a FROM Account a WHERE YEAR(current_date) - YEAR(a.createdAt) > :expiredAge")
    List<Account> findExpiredAccounts(final Integer expiredAge);
}
