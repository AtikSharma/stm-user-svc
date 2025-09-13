package com.taskmanager.usermanagement.mapper;

import java.util.List;

import com.taskmanager.usermanagement.model.request.UserUpdateRequest;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;

import com.taskmanager.common.model.User;
import com.taskmanager.common.model.UserBase;
import com.taskmanager.common.model.UserExp;
import com.taskmanager.common.model.request.RegistrationRequest;

@Mapper(componentModel = "spring")
public interface UserBOMapper {

	User mapFromRegistrationRequest(RegistrationRequest registrationRequest);

	UserExp mapToRegistrationResponse(User user);

	UserBase mapFrom(User user);

	List<UserBase> mapToUserBase(List<User> users);

	User mapFromUpdateRequest(UserUpdateRequest userUpdateRequest);

}
