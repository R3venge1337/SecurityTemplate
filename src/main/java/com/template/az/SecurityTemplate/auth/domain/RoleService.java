package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.auth.RoleFacade;
import com.template.az.SecurityTemplate.auth.dto.CreateRoleForm;
import com.template.az.SecurityTemplate.auth.dto.RoleDto;
import com.template.az.SecurityTemplate.auth.dto.RoleFilterForm;
import com.template.az.SecurityTemplate.auth.dto.UpdateRoleForm;
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
class RoleService implements RoleFacade {

    private final RoleRepository roleRepository;

    @Override
    public PageDto<RoleDto> findAllRoles(final RoleFilterForm filterForm, final PageableRequest pageableRequest) {
        DtoValidator.validate(filterForm);
        DtoValidator.validate(pageableRequest);

        final RoleSpecification specification = new RoleSpecification(filterForm);
        final Page<RoleDto> accounts = roleRepository.findAll(specification, PageableUtils.createPageable(pageableRequest))
                .map(this::mapToDto);

        return PageableUtils.toDto(accounts);
    }

    @Override
    public RoleDto findRole(final Long id) {
        return roleRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ROLE_NOT_FOUND, id));
    }

    @Override
    @Transactional
    public IdDto saveRole(final CreateRoleForm createForm) {
        DtoValidator.validate(createForm);

        final Role role = new Role();
        role.setName(createForm.name());
        role.setDescription(createForm.description());

        return new IdDto(roleRepository.save(role).getId());
    }

    @Override
    @Transactional
    public void updateRole(final Long id, final UpdateRoleForm updateForm) {
        DtoValidator.validate(updateForm);

        Role roleFetched = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ROLE_NOT_FOUND, id));

        checkUnique(updateForm.name(), roleFetched.getName());

        roleFetched.setName(updateForm.name());
        roleFetched.setDescription(updateForm.name());
    }

    @Override
    @Transactional
    public void deleteRole(final Long id) {
        roleRepository.deleteById(id);
    }

    private RoleDto mapToDto(final Role role) {
        return new RoleDto(role.getId(), role.getName(), role.getDescription(), role.getCreatedAt());
    }

    private void checkUnique(final String formName, final String entityName) {
        if (!formName.equals(entityName)) {
            checkUnique(formName);
        }
    }

    void checkUnique(final String name) {
        if (roleRepository.existsByName(name)) {
            throw new NotUniqueException(Role.Fields.name, ErrorMessages.ROLE_NOT_FOUND);
        }
    }
}
