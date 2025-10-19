package com.taskmanager.usermanagement.mapper;

import com.taskmanager.common.model.User;
import com.taskmanager.common.model.UserBase;
import com.taskmanager.common.model.request.RegistrationRequest;
import com.taskmanager.usermanagement.model.request.UserUpdateRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserBOMapper {

	User mapFromRegistrationRequest(RegistrationRequest registrationRequest);

	UserBase mapFrom(User user);

	List<UserBase> mapToUserBase(List<User> users);

	User mapFromUpdateRequest(UserUpdateRequest userUpdateRequest);

}
