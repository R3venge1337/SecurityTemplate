package com.template.az.SecurityTemplate.auth.domain;

import com.template.az.SecurityTemplate.auth.UserFacade;
import com.template.az.SecurityTemplate.auth.dto.FilterUserForm;
import com.template.az.SecurityTemplate.auth.dto.RoleIdsForm;
import com.template.az.SecurityTemplate.auth.dto.UpdateUserForm;
import com.template.az.SecurityTemplate.auth.dto.UserResponse;
import com.template.az.SecurityTemplate.auth.dto.UserWithAccount;
import com.template.az.SecurityTemplate.common.controller.PageDto;
import com.template.az.SecurityTemplate.common.controller.PageableRequest;
import com.template.az.SecurityTemplate.common.controller.PageableUtils;
import com.template.az.SecurityTemplate.common.exception.NotFoundException;
import com.template.az.SecurityTemplate.common.validation.DtoValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

import static com.template.az.SecurityTemplate.auth.domain.DomainMapper.mapToUserResponse;
import static com.template.az.SecurityTemplate.auth.domain.DomainMapper.maptoUserWithAccount;
import static com.template.az.SecurityTemplate.common.exception.error.ErrorMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
class UserService implements UserFacade {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserResponse updateUser(final UUID uuid, final UpdateUserForm updateForm) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND, uuid));

        user.setFirstName(updateForm.name());
        user.setSecondName(updateForm.secondName());
        user.setSurname(updateForm.surname());
        user.setYearOfBirth(updateForm.yearOfBirth());

        return mapToUserResponse(user);
    }

    @Override
    public PageDto<UserResponse> getAllUsers(final FilterUserForm filterForm, final PageableRequest pageableRequest) {
        DtoValidator.validate(filterForm);
        DtoValidator.validate(pageableRequest);

        final UserSpecification userSpecification = new UserSpecification(filterForm);

        final Page<UserResponse> users = userRepository.findAll(userSpecification, PageableUtils.createPageable(pageableRequest))
                .map(DomainMapper::mapToUserResponse);

        return PageableUtils.toDto(users);
    }

    @Override
    @Transactional
    public UserWithAccount findByUsername(final String username) {
        return maptoUserWithAccount(userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND, username)));
    }

    @Override
    public UserResponse findByUuid(final UUID uuid) {
        return mapToUserResponse(userRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND, uuid)));
    }


    @Override
    @Transactional
    public void assignRolesToUser(final UUID userUuid, final RoleIdsForm idsForm) {
        Validate.notNull(idsForm, "RoleUuidForm cannot be null");
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND, userUuid));
        Set<Role> roles = roleRepository.findRolesByIdIn(idsForm.ids());
        user.getAccount().getRoles().addAll(roles);
    }

    @Override
    @Transactional
    public void unassignRolesToUser(final UUID userUuid, final RoleIdsForm idsForm) {
        Validate.notNull(idsForm, "RoleUuidForm cannot be null");
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND, userUuid));
        Set<Role> roles = roleRepository.findRolesByIdIn(idsForm.ids());
        user.getAccount().getRoles().addAll(roles);
    }

    @Override
    public void deleteByUuid(final UUID uuid) {
        userRepository.deleteByUuid(uuid);
    }
}
