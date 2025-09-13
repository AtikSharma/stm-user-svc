package com.taskmanager.usermanagement.dao.impl;

import java.util.List;
import java.util.Optional;

import com.taskmanager.common.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taskmanager.common.model.User;
import com.taskmanager.usermanagement.dao.UserDao;
import com.taskmanager.usermanagement.entity.UserEntity;
import com.taskmanager.usermanagement.mapper.UserEntityMapper;
import com.taskmanager.usermanagement.repository.UserRepository;

@Component
public class UserDaoImpl implements UserDao {

    private final UserRepository userRepository;

    private final UserEntityMapper userEntityMapper;

    @Autowired
    public UserDaoImpl(UserRepository userRepository, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        return userEntityOptional.map(userEntityMapper::mapFrom);
    }

    @Override
    public User registerUser(User user) {
        UserEntity userEntity = userEntityMapper.mapTo(user);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return userEntityMapper.mapFrom(savedUserEntity);
    }

    @Override
    public User updateUser(User user) {
        UserEntity userEntity = userEntityMapper.mapTo(user);
        UserEntity updatedUserEntity = userRepository.save(userEntity);
        return userEntityMapper.mapFrom(updatedUserEntity);
    }

    @Override
    public User deleteUser(User user) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<User> getAllUsers(Boolean includeInactive) {
        List<UserEntity> userEntities = userRepository.findAllByStatusIn(includeInactive != null && includeInactive.equals(true) ? List.of(Status.INACTIVE) : List.of(Status.ACTIVE, Status.BLOCKED));
        return userEntityMapper.mapFromUserEntities(userEntities);
    }

    @Override
    public Optional<User> getUserById(String id) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);
        return userEntityOptional.map(userEntityMapper::mapFrom);
    }

}
