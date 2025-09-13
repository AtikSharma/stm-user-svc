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

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserDetailsByUsername(String username) throws ApplicationException {
        if (StringUtils.isBlank(username)) {
            throw new ApplicationException(ErrorConstants.ERROR_INVALID_USERNAME, username);
        }
        return userDao.getUserByUsername(username)
                .orElseThrow(() -> new ApplicationException(ErrorConstants.ERROR_USER_NOT_FOUND_USERNAME, username));
    }

    @Override
    public User userRegistration(User newUserDetails) {
        LocalDateTime now = LocalDateTime.now();
        return userDao.registerUser(newUserDetails.toBuilder().status(Status.ACTIVE).createdAt(now).updatedAt(now).build());
    }

    @Override
    public User updateUser(User user) {
        User existingUser = getUserDetailsById(user.getId());

        // Partial update: only update fields that are non-null/non-blank in the incoming user
        if (user.getUsername() != null && !StringUtils.isBlank(user.getUsername())) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getEmail() != null && !StringUtils.isBlank(user.getEmail())) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null && !StringUtils.isBlank(user.getPassword())) {
            existingUser.setPassword(user.getPassword());
        }
        // Do not update createdAt
        existingUser.setUpdatedAt(LocalDateTime.now());

        return userDao.updateUser(existingUser);
    }

    @Override
    public User deleteUser(User user) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<User> getAllUsers(Boolean includeInactive) {
        return userDao.getAllUsers(includeInactive);
    }

    @Override
    public User getUserDetailsById(String id) throws ApplicationException {
        if (StringUtils.isBlank(id)) {
            throw new ApplicationException(ErrorConstants.ERROR_INVALID_ID, id);
        }
        return userDao.getUserById(id)
                .orElseThrow(() -> new ApplicationException(ErrorConstants.ERROR_USER_NOT_FOUND_USERNAME, id));
    }

}
