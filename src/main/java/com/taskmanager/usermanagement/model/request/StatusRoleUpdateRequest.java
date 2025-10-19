package com.taskmanager.usermanagement.model.request;

import com.taskmanager.common.enums.Role;
import com.taskmanager.common.enums.Status;
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
public class StatusRoleUpdateRequest {

    private Status status;

    private Role role;
}
