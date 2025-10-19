package com.taskmanager.usermanagement.service.impl;

import com.taskmanager.common.constants.ErrorConstants;
import com.taskmanager.common.enums.Status;
import com.taskmanager.common.exception.ApplicationException;
import com.taskmanager.common.model.User;
import com.taskmanager.common.util.StringUtils;
import com.taskmanager.usermanagement.constants.AuditLogConstants;
import com.taskmanager.usermanagement.dao.UserDao;
import com.taskmanager.usermanagement.model.request.StatusRoleUpdateRequest;
import com.taskmanager.usermanagement.service.UserService;
import com.taskmanager.usermanagement.util.AuditLogUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final AuditLogUtility auditLogUtility;

    @Autowired
    public UserServiceImpl(UserDao userDao, AuditLogUtility auditLogUtility) {
        this.userDao = userDao;
        this.auditLogUtility = auditLogUtility;
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
    @Transactional
    public User userRegistration(User newUserDetails) {
        LocalDateTime now = LocalDateTime.now();
        User newUser = userDao.registerUser(newUserDetails.toBuilder().status(Status.ACTIVE).createdAt(now).updatedAt(now).build());
        auditLogUtility.logAction(AuditLogConstants.ACTION_USER_REGISTER, newUser.getId());
        return newUser;
    }

    @Override
    @Transactional
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

        User updatedUser = userDao.updateUser(existingUser);

        auditLogUtility.logAction(AuditLogConstants.ACTION_USER_UPDATE, updatedUser.getId());
        return updatedUser;
    }

    @Override
    public List<User> getAllUsers(Boolean includeInactive, Boolean isDetailsRequired) {
        List<User> users = userDao.getAllUsers(includeInactive);
        if (isDetailsRequired != null && !isDetailsRequired) {
            users.forEach(user -> {
                user.setEmail(null);
                user.setPassword(null);
            }); // Remove sensitive details
        }
        return users;
    }

    @Override
    public User getUserDetailsById(String id) throws ApplicationException {
        if (StringUtils.isBlank(id)) {
            throw new ApplicationException(ErrorConstants.ERROR_INVALID_ID, id);
        }
        return userDao.getUserById(id)
                .orElseThrow(() -> new ApplicationException(ErrorConstants.ERROR_USER_NOT_FOUND_USERNAME, id));
    }

    @Override
    @Transactional
    public void updateStatusRole(String userId, StatusRoleUpdateRequest statusRoleUpdateRequest) {
        User existingUser = getUserDetailsById(userId);

        if (statusRoleUpdateRequest.getStatus() != null) {
            existingUser.setStatus(statusRoleUpdateRequest.getStatus());
        }
        if (statusRoleUpdateRequest.getRole() != null) {
            existingUser.setRole(statusRoleUpdateRequest.getRole());
        }

        existingUser.setUpdatedAt(LocalDateTime.now());
        userDao.updateUser(existingUser);
        auditLogUtility.logAction(AuditLogConstants.ACTION_USER_STATUS_ROLE_UPDATE, existingUser.getId());
    }

}
