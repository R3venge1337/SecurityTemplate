package com.template.az.SecurityTemplate.auth;

import com.template.az.SecurityTemplate.auth.dto.CreateRoleForm;
import com.template.az.SecurityTemplate.auth.dto.RoleDto;
import com.template.az.SecurityTemplate.auth.dto.RoleFilterForm;
import com.template.az.SecurityTemplate.auth.dto.UpdateRoleForm;
import com.template.az.SecurityTemplate.common.controller.IdDto;
import com.template.az.SecurityTemplate.common.controller.PageDto;
import com.template.az.SecurityTemplate.common.controller.PageableRequest;

public interface RoleFacade {
    PageDto<RoleDto> findAllRoles(final RoleFilterForm filterForm, final PageableRequest pageableRequest);

    RoleDto findRole(final Long id);

    RoleDto findRoleByName(final String name);

    IdDto saveRole(final CreateRoleForm createForm);

    void updateRole(final Long id, final UpdateRoleForm updateForm);

    void deleteRole(final Long id);
}
