package com.taskmanager.usermanagement.dao;

import java.util.List;
import java.util.Optional;

import com.taskmanager.common.model.User;

public interface UserDao {

	public Optional<User> getUserByUsername(String username);

	public User registerUser(User user);

	public User updateUser(User user);

	public User deleteUser(User user);

	public List<User> getAllUsers(Boolean includeInactive);

	public Optional<User> getUserById(String id);
}
