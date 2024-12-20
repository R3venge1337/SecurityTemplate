package com.template.az.SecurityTemplate.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Boolean existsByName(final String name);

    Optional<Role> findByName(final String name);

    Set<Role> findRolesByIdIn(final List<Long> ids);
}
