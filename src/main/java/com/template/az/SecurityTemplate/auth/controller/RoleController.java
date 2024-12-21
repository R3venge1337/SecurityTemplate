package com.template.az.SecurityTemplate.auth.controller;

import com.template.az.SecurityTemplate.auth.RoleFacade;
import com.template.az.SecurityTemplate.auth.dto.CreateRoleForm;
import com.template.az.SecurityTemplate.auth.dto.RoleDto;
import com.template.az.SecurityTemplate.auth.dto.RoleFilterForm;
import com.template.az.SecurityTemplate.auth.dto.UpdateRoleForm;
import com.template.az.SecurityTemplate.common.controller.IdDto;
import com.template.az.SecurityTemplate.common.controller.PageDto;
import com.template.az.SecurityTemplate.common.controller.PageableRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.template.az.SecurityTemplate.auth.controller.RoleController.Routes.ROOT;
import static com.template.az.SecurityTemplate.auth.controller.RoleController.Routes.ROOT_ID;

@RestController
@RequiredArgsConstructor
class RoleController {

    static final class Routes {
        static final String ROOT = "/roles";
        static final String ROOT_ID = "/roles/{id}";
    }

    private final RoleFacade roleFacade;

    @GetMapping(ROOT)
    PageDto<RoleDto> findRoles(@RequestBody final RoleFilterForm filterForm, final PageableRequest pageableRequest) {
        return roleFacade.findAllRoles(filterForm, pageableRequest);
    }

    @GetMapping(ROOT_ID)
    RoleDto findRole(@PathVariable final Long id) {
        return roleFacade.findRole(id);
    }

    @PostMapping(ROOT)
    @ResponseStatus(HttpStatus.CREATED)
    IdDto createRole(@RequestBody final CreateRoleForm createForm) {
        return roleFacade.saveRole(createForm);
    }


    @PutMapping(ROOT_ID)
    void updateRole(@PathVariable final Long id,
                    @RequestBody final UpdateRoleForm updateForm) {
        roleFacade.updateRole(id, updateForm);
    }

    @DeleteMapping(ROOT_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteRole(@PathVariable final Long id) {
        roleFacade.deleteRole(id);
    }
}
