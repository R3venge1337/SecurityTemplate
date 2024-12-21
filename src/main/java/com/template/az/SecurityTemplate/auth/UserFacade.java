package com.template.az.SecurityTemplate.auth;

import com.template.az.SecurityTemplate.auth.dto.FilterUserForm;
import com.template.az.SecurityTemplate.auth.dto.RoleIdsForm;
import com.template.az.SecurityTemplate.auth.dto.UpdateUserForm;
import com.template.az.SecurityTemplate.auth.dto.UserResponse;
import com.template.az.SecurityTemplate.auth.dto.UserWithAccount;
import com.template.az.SecurityTemplate.common.controller.PageDto;
import com.template.az.SecurityTemplate.common.controller.PageableRequest;

import java.util.UUID;

public interface UserFacade {

    UserResponse updateUser(final UUID uuid, final UpdateUserForm updateForm);

    PageDto<UserResponse> getAllUsers(final FilterUserForm filterForm, final PageableRequest pageableRequest);

    UserWithAccount findByUsername(final String username);

    UserResponse findByUuid(final UUID uuid);

    void assignRolesToUser(final UUID userUuid, final RoleIdsForm uuids);

    void unassignRolesToUser(final UUID userUuid, final RoleIdsForm uuids);

    void deleteByUuid(final UUID uuid);
}
