package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.common.entity.AbstractUUIDEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Entity
@Table(name = "appuser")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@FieldNameConstants
class User extends AbstractUUIDEntity {

    @NotNull
    @Column(name = "first_name", nullable = false)
    @Size(min = 2, max = 30, message = "{error.size.firstName}")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @NotNull
    @Size(min = 2, max = 30)
    @Column(name = "surname", nullable = false)
    private String surname;

    private Short yearOfBirth;

    @Size(min = 9, max = 9)
    private String telephone;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}
