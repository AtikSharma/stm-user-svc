package com.taskmanager.usermanagement.entity;

import com.taskmanager.common.enums.Role;
import com.taskmanager.common.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@Document(collection = "user_db")
@NoArgsConstructor
public class UserEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    private String firstName;

    @Indexed(unique = true)
    private String lastName;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    @NotNull(message = "password must not be null")
    private String password;

    @NotNull(message = "Role must not be null")
    private Role role;

    @NotNull(message = "Status must not be null")
    private Status status = Status.ACTIVE;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
