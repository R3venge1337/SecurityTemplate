package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.auth.PermissionFacade;
import com.template.az.SecurityTemplate.auth.dto.CreatePermissionForm;
import com.template.az.SecurityTemplate.auth.dto.PermissionDto;
import com.template.az.SecurityTemplate.auth.dto.PermissionFilterForm;
import com.template.az.SecurityTemplate.auth.dto.UpdatePermissionForm;
import com.template.az.SecurityTemplate.common.controller.IdDto;
import com.template.az.SecurityTemplate.common.controller.PageDto;
import com.template.az.SecurityTemplate.common.controller.PageableRequest;
import com.template.az.SecurityTemplate.common.controller.PageableUtils;
import com.template.az.SecurityTemplate.common.exception.NotFoundException;
import com.template.az.SecurityTemplate.common.exception.NotUniqueException;
import com.template.az.SecurityTemplate.common.exception.error.ErrorMessages;
import com.template.az.SecurityTemplate.common.validation.DtoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@RequiredArgsConstructor
class PermissionService implements PermissionFacade {

    private final PermissionRepository permissionRepository;

    @Override
    public PageDto<PermissionDto> findAllPermissions(final PermissionFilterForm filterForm, final PageableRequest pageableRequest) {
        DtoValidator.validate(filterForm);
        DtoValidator.validate(pageableRequest);

        final PermissionSpecification specification = new PermissionSpecification(filterForm);
        final Page<PermissionDto> permissions = permissionRepository.findAll(specification, PageableUtils.createPageable(pageableRequest))
                .map(this::createPermissionDto);

        return PageableUtils.toDto(permissions);
    }

    @Override
    public PermissionDto findPermission(final Long id) {
        return permissionRepository.findById(id)
                .map(this::createPermissionDto)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.PERMISSION_NOT_FOUND, id));
    }

    @Override
    @Transactional
    public IdDto savePermission(final CreatePermissionForm createForm) {
        DtoValidator.validate(createForm);

        checkUnique(createForm.name());

        final Permission permission = new Permission();
        permission.setName(createForm.name());
        permission.setDescription(createForm.description());

        return new IdDto(permissionRepository.save(permission).getId());
    }

    @Override
    @Transactional
    public void updatePermission(final Long id, final UpdatePermissionForm updateForm) {
        DtoValidator.validate(updateForm);

        Permission permissionFetched = permissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.PERMISSION_NOT_FOUND, id));

        checkUnique(updateForm.name(), permissionFetched.getName());

        permissionFetched.setName(updateForm.name());
        permissionFetched.setDescription(updateForm.name());
    }

    @Override
    @Transactional
    public void deletePermission(final Long id) {
        permissionRepository.deleteById(id);
    }

    private PermissionDto createPermissionDto(final Permission permission) {
        return new PermissionDto(permission.getId(), permission.getName(), permission.getCreatedAt());
    }

    private void checkUnique(final String formName, final String entityName) {
        if (!formName.equals(entityName)) {
            checkUnique(formName);
        }
    }

    void checkUnique(final String name) {
        if (permissionRepository.existsByName(name)) {
            throw new NotUniqueException(Permission.Fields.name, ErrorMessages.PERMISSION_IS_TAKEN);
        }
    }
}
