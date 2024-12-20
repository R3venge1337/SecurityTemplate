package com.template.az.SecurityTemplate.auth;

import com.template.az.SecurityTemplate.auth.dto.AuthenticationRequest;
import com.template.az.SecurityTemplate.auth.dto.AuthenticationResponse;
import com.template.az.SecurityTemplate.auth.dto.CreateUserForm;
import com.template.az.SecurityTemplate.auth.dto.UserResponse;

public interface AuthenticationFacade {

    AuthenticationResponse authenticateUser(final AuthenticationRequest request);

    UserResponse registerUser(final CreateUserForm createForm);

    void verifyUser(final String verificationCode);

    void resendVerificationCode(final String email);
}
