package com.taskmanager.usermanagement.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.common.constants.ErrorConstants;
import com.taskmanager.common.enums.Status;
import com.taskmanager.common.exception.ApplicationException;
import com.taskmanager.common.model.User;
import com.taskmanager.common.util.StringUtils;
import com.taskmanager.usermanagement.dao.UserDao;
import com.taskmanager.usermanagement.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private UserDao userDao;

	@Autowired
	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User getUserDetails(String identifier) throws ApplicationException {
		if (StringUtils.isBlank(identifier)) {
			throw new ApplicationException(ErrorConstants.ERROR_INVALID_IDENTIFIER, identifier);
		}
		return userDao.getUser(identifier)
				.orElseThrow(() -> new ApplicationException(ErrorConstants.ERROR_USER_NOT_FOUND, identifier));
	}

	@Override
	public User userRegistration(User user) {
		LocalDateTime now = LocalDateTime.now();
		user = user.toBuilder().status(Status.ACTIVE).createdAt(now).updatedAt(now).build();
		return userDao.registerUser(user);
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User deleteUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getAllUsers() {
		return userDao.getAllUsers();
	}

}
