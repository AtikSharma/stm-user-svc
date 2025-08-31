package com.taskmanager.usermanagement.model.response;

import java.util.List;

import com.taskmanager.common.model.ServiceResponse;
import com.taskmanager.common.model.UserBase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class UsersDetailResponse extends ServiceResponse {

	List<UserBase> users;
}
