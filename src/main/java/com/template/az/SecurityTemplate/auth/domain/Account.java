package com.template.az.SecurityTemplate.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.template.az.SecurityTemplate.common.entity.AbstractUUIDEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "account")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@FieldNameConstants
class Account extends AbstractUUIDEntity {

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    @JsonIgnore
    private String password;

    @NotNull
    @Email
    private String email;

    private Boolean enabled;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    private String verificationCode;

    private LocalDateTime verificationCodeExpiredAt;

    private String resetPasswordCode;

    private LocalDateTime resetPasswordCodeExpiredAt;

    private Boolean passwordResetCompleted;

    private Boolean accountNonLocked;

    private int failedAttempt;

    private LocalDateTime lockTime;

    private LocalDateTime passwordLastTimeChanged;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Setter(AccessLevel.NONE)
    private Set<Role> roles = new HashSet<>(0);

    public void addRole(final Role role) {
        roles.add(role);
        role.getAccounts().add(this);
    }

    public void removeRole(final Role role) {
        roles.remove(role);
        role.getAccounts().remove(this);
    }
}
