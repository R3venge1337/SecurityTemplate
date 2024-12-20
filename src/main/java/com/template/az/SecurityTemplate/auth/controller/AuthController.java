package com.template.az.SecurityTemplate.auth.controller;

import com.template.az.SecurityTemplate.auth.AuthenticationFacade;
import com.template.az.SecurityTemplate.auth.dto.AuthenticationRequest;
import com.template.az.SecurityTemplate.auth.dto.AuthenticationResponse;
import com.template.az.SecurityTemplate.auth.dto.CreateUserForm;
import com.template.az.SecurityTemplate.auth.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.template.az.SecurityTemplate.auth.controller.AuthController.Routes.AUTHENTICATION;
import static com.template.az.SecurityTemplate.auth.controller.AuthController.Routes.EMAIL_RESEND;
import static com.template.az.SecurityTemplate.auth.controller.AuthController.Routes.REGISTRATION;
import static com.template.az.SecurityTemplate.auth.controller.AuthController.Routes.VERIFICATION;

@RestController
@RequiredArgsConstructor
class AuthController {

    static final class Routes {
        static final String ROOT = "/auth";
        static final String AUTHENTICATION = ROOT + "/authenticate";
        static final String REGISTRATION = ROOT + "/register";
        static final String VERIFICATION = ROOT + "/verify-email";
        static final String EMAIL_RESEND = ROOT + "/resend/{email}";
    }

    private final AuthenticationFacade authenticationFacade;

    @PostMapping(AUTHENTICATION)
    AuthenticationResponse authenticate(@RequestBody final AuthenticationRequest request) {
        return authenticationFacade.authenticateUser(request);
    }

    @PostMapping(REGISTRATION)
    @ResponseStatus(HttpStatus.CREATED)
    UserResponse registerUser(@RequestBody final CreateUserForm createForm) {
        return authenticationFacade.registerUser(createForm);
    }

    @GetMapping(VERIFICATION)
    void verifyUser(@RequestParam("token") final String token) {
        authenticationFacade.verifyUser(token);
    }

    @PostMapping(EMAIL_RESEND)
    void resendVerificationCode(@PathVariable(name = "email") final String email) {
        authenticationFacade.resendVerificationCode(email);
    }

}
