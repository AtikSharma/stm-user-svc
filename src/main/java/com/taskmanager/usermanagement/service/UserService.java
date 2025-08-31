package com.taskmanager.usermanagement.service;

import java.util.List;

import com.taskmanager.common.exception.ApplicationException;
import com.taskmanager.common.model.User;

public interface UserService {

	public User userRegistration(User user);

	public User updateUser(User user);

	public User deleteUser(User user);

	public User getUserDetails(String identifier) throws ApplicationException;

	public List<User> getAllUsers();
}
