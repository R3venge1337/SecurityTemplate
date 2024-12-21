package com.template.az.SecurityTemplate.auth.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findByUuid(final UUID uuid);

    @Query(value = "SELECT u FROM User u INNER JOIN u.account acc WHERE acc.username = :username")
    Optional<User> findByUsername(final @Param("username") String username);

    void deleteByUuid(final UUID uuid);
}
