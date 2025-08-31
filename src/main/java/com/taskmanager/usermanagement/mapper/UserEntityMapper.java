package com.taskmanager.usermanagement.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.taskmanager.common.model.User;
import com.taskmanager.common.util.PasswordUtil;
import com.taskmanager.usermanagement.entity.UserEntity;

@Mapper(componentModel = "spring", imports = { PasswordUtil.class })
public interface UserEntityMapper {

	@Mapping(target = "password", source = "password", qualifiedByName = "encrypt")
	UserEntity mapTo(User user);

	User mapFrom(UserEntity userEntity);

	List<User> mapFromUserEntities(List<UserEntity> userEntities);

	@Named("encrypt")
	default String encrypt(String value) {
		return PasswordUtil.encrypt(value);
	}

}
