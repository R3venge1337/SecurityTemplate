package com.template.az.SecurityTemplate.auth.controller;

import com.template.az.SecurityTemplate.auth.PermissionFacade;
import com.template.az.SecurityTemplate.auth.dto.CreatePermissionForm;
import com.template.az.SecurityTemplate.auth.dto.PermissionDto;
import com.template.az.SecurityTemplate.auth.dto.PermissionFilterForm;
import com.template.az.SecurityTemplate.auth.dto.UpdatePermissionForm;
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

import static com.template.az.SecurityTemplate.auth.controller.PermissionController.Routes.ROOT;
import static com.template.az.SecurityTemplate.auth.controller.PermissionController.Routes.ROOT_ID;

@RestController
@RequiredArgsConstructor
class PermissionController {

    static final class Routes {
        static final String ROOT = "/permissions";
        static final String ROOT_ID = ROOT + "/{id}";
    }

    private final PermissionFacade permissionFacade;

    @GetMapping(ROOT)
    PageDto<PermissionDto> findPermissions(@RequestBody final PermissionFilterForm filterForm, final PageableRequest pageableRequest) {
        return permissionFacade.findAllPermissions(filterForm, pageableRequest);
    }

    @GetMapping(ROOT_ID)
    PermissionDto findPermission(@PathVariable final Long id) {
        return permissionFacade.findPermission(id);
    }

    @PostMapping(ROOT)
    @ResponseStatus(HttpStatus.CREATED)
    IdDto createPermission(@RequestBody final CreatePermissionForm createForm) {
        return permissionFacade.savePermission(createForm);
    }


    @PutMapping(ROOT_ID)
    void updatePermission(@PathVariable final Long id,
                          @RequestBody final UpdatePermissionForm updateForm) {
        permissionFacade.updatePermission(id, updateForm);
    }

    @DeleteMapping(ROOT_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deletePermission(@PathVariable final Long id) {
        permissionFacade.deletePermission(id);
    }
}
