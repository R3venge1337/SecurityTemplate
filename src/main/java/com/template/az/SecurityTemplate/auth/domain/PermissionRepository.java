package com.template.az.SecurityTemplate.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    Boolean existsByName(final String name);

    @Query("SELECT p FROM Permission p WHERE p.name IN :roleNames")
    Set<Permission> findRolesByNames(@Param("roleNames") Set<String> roleNames);
}
