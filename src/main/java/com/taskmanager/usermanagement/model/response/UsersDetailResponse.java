package com.taskmanager.usermanagement.model.response;

import com.taskmanager.common.model.ServiceResponse;
import com.taskmanager.common.model.User;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class UsersDetailResponse extends ServiceResponse {

	List<User> users;
}
