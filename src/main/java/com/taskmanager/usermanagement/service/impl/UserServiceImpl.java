package com.taskmanager.usermanagement.service.impl;

import com.taskmanager.common.constants.ErrorConstants;
import com.taskmanager.common.enums.Status;
import com.taskmanager.common.exception.ApplicationException;
import com.taskmanager.common.model.User;
import com.taskmanager.common.util.StringUtils;
import com.taskmanager.usermanagement.constants.AuditLogConstants;
import com.taskmanager.usermanagement.dao.UserDao;
import com.taskmanager.usermanagement.mapper.UserBOMapper;
import com.taskmanager.usermanagement.model.request.StatusRoleUpdateRequest;
import com.taskmanager.usermanagement.service.UserService;
import com.taskmanager.usermanagement.util.AuditLogUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final AuditLogUtility auditLogUtility;
    private final UserBOMapper userBOMapper;

    @Autowired
    public UserServiceImpl(UserDao userDao, AuditLogUtility auditLogUtility, UserBOMapper userBOMapper) {
        this.userDao = userDao;
        this.auditLogUtility = auditLogUtility;
        this.userBOMapper = userBOMapper;
    }

    @Override
    public User getUserDetailsByUsername(String username, Boolean isDetailsRequired) throws ApplicationException {
        if (StringUtils.isBlank(username)) {
            throw new ApplicationException(ErrorConstants.ERROR_INVALID_USERNAME, username);
        }
        User user = userDao.getUserByUsername(username)
                .orElseThrow(() -> new ApplicationException(ErrorConstants.ERROR_USER_NOT_FOUND_USERNAME, username));
        return (User) removeSensitiveInfo(isDetailsRequired, null, user);
    }

    @Override
    @Transactional
    public User userRegistration(User newUserDetails) {
        LocalDateTime now = LocalDateTime.now();
        User newUser = userDao.registerUser(newUserDetails.toBuilder().status(Status.ACTIVE).createdAt(now).createdBy("User").build());
        auditLogUtility.logAction(AuditLogConstants.ACTION_USER_REGISTER, newUser.getId());
        return newUser;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        User existingUser = getUserDetailsById(user.getId(), true);
        userBOMapper.updateUserFromDTO(user, existingUser);
        User updatedUser = userDao.updateUser(existingUser);
        auditLogUtility.logAction(AuditLogConstants.ACTION_USER_UPDATE, updatedUser.getId());
        return updatedUser;
    }

    @Override
    public List<User> getAllUsers(Boolean includeInactive, Boolean isDetailsRequired) {
        List<User> users = userDao.getAllUsers(includeInactive);
        return (List<User>) removeSensitiveInfo(isDetailsRequired, users, null);
    }

    private Object removeSensitiveInfo(Boolean isDetailsRequired, List<User> users, User user) {
        if (isDetailsRequired != null && !isDetailsRequired) {
            if (user != null) {
                return userBOMapper.removeSensitiveInfo(user);
            } else if (!CollectionUtils.isEmpty(users)) {
                return users.stream().map(userBOMapper::removeSensitiveInfo).toList();
            }
        }
        return user != null ? user : users;
    }

    @Override
    public User getUserDetailsById(String id, Boolean isDetailsRequired) throws ApplicationException {
        if (StringUtils.isBlank(id)) {
            throw new ApplicationException(ErrorConstants.ERROR_INVALID_ID, id);
        }
        User user = userDao.getUserById(id)
                .orElseThrow(() -> new ApplicationException(ErrorConstants.ERROR_USER_NOT_FOUND_USERNAME, id));
        return (User) removeSensitiveInfo(isDetailsRequired, null, user);
    }

    @Override
    @Transactional
    public void updateStatusRole(String userId, StatusRoleUpdateRequest statusRoleUpdateRequest, String updatedBy) {
        User existingUser = getUserDetailsById(userId, true);

        if (statusRoleUpdateRequest.getStatus() != null) {
            existingUser.setStatus(statusRoleUpdateRequest.getStatus());
        }
        if (statusRoleUpdateRequest.getRole() != null) {
            existingUser.setRole(statusRoleUpdateRequest.getRole());
        }

        existingUser.setUpdatedAt(LocalDateTime.now());
        existingUser.setUpdatedBy(updatedBy);
        userDao.updateUser(existingUser);
        auditLogUtility.logAction(AuditLogConstants.ACTION_USER_STATUS_ROLE_UPDATE, existingUser.getId());
    }

}
