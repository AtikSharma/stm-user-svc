package com.taskmanager.usermanagement.util;

import com.taskmanager.common.model.request.RegistrationRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserValidator {

    public void validateRequest(RegistrationRequest registrationRequest) {
        isFirstNameAndLastNameExists(registrationRequest);
        isUserNameAndPasswordExists(registrationRequest);
    }

    private void isUserNameAndPasswordExists(RegistrationRequest registrationRequest) {
        if (Objects.isNull(registrationRequest.getUsername()) || registrationRequest.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (Objects.isNull(registrationRequest.getPassword()) || registrationRequest.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
    }

    private void isFirstNameAndLastNameExists(RegistrationRequest registrationRequest) {
        if (Objects.isNull(registrationRequest.getFirstName()) || registrationRequest.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (Objects.isNull(registrationRequest.getLastName()) || registrationRequest.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
    }

}
