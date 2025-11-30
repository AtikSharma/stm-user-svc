package com.taskmanager.usermanagement.model.request;

import com.taskmanager.common.enums.Status;
import com.taskmanager.common.model.request.RegistrationRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserUpdateRequest {

    @Schema(description = "First Name of the user", example = "John")
    private String firstName;

    @Schema(description = "Last Name of the user", example = "Doe")
    private String lastName;

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @Schema(description = "Email address of the user", example = "john@example.com")
    private String email;

    @Schema(description = "Password of the user", example = "securePassword123")
    private String password;

}
