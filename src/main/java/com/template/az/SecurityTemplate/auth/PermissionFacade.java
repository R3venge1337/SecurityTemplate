package com.template.az.SecurityTemplate.auth;

import com.template.az.SecurityTemplate.auth.dto.CreatePermissionForm;
import com.template.az.SecurityTemplate.auth.dto.PermissionDto;
import com.template.az.SecurityTemplate.auth.dto.PermissionFilterForm;
import com.template.az.SecurityTemplate.auth.dto.UpdatePermissionForm;
import com.template.az.SecurityTemplate.common.controller.IdDto;
import com.template.az.SecurityTemplate.common.controller.PageDto;
import com.template.az.SecurityTemplate.common.controller.PageableRequest;

public interface PermissionFacade {
    PageDto<PermissionDto> findAllPermissions(final PermissionFilterForm filterForm, final PageableRequest pageableRequest);

    PermissionDto findPermission(final Long id);

    IdDto savePermission(final CreatePermissionForm createForm);

    void updatePermission(final Long id, final UpdatePermissionForm updateForm);

    void deletePermission(final Long id);
}
