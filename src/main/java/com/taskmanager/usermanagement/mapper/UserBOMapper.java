package com.taskmanager.usermanagement.mapper;

import com.taskmanager.common.model.User;
import com.taskmanager.common.model.UserBase;
import com.taskmanager.common.model.request.RegistrationRequest;
import com.taskmanager.usermanagement.model.request.UserUpdateRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", imports = {java.time.LocalDateTime.class})
public interface UserBOMapper {

    User mapFromRegistrationRequest(RegistrationRequest registrationRequest);

    UserBase mapFrom(User user);

    List<User> mapToUserBase(List<User> users);

    @Mapping(target = "id", source = "userId")
    User mapFromUpdateRequest(UserUpdateRequest userUpdateRequest, String userId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDTO(User user, @MappingTarget User existingUser);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    User removeSensitiveInfo(User user);
}
