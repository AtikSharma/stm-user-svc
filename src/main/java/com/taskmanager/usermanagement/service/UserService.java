package com.taskmanager.usermanagement.service;

import java.util.List;

import com.taskmanager.common.exception.ApplicationException;
import com.taskmanager.common.model.User;
import com.taskmanager.usermanagement.model.request.StatusRoleUpdateRequest;

public interface UserService {

	public User userRegistration(User user);

	public User updateUser(User user);

	public User getUserDetailsByUsername(String username) throws ApplicationException;

	public List<User> getAllUsers(Boolean includeInactive, Boolean isDetailsRequired);

	public User getUserDetailsById(String id) throws ApplicationException;

	public void updateStatusRole(String id, StatusRoleUpdateRequest statusRoleUpdateRequest);
}
