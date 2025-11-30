package com.taskmanager.usermanagement.repository;

import com.taskmanager.common.enums.Status;
import com.taskmanager.usermanagement.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String>, QueryByExampleExecutor<UserEntity> {

    List<UserEntity> findAllByStatusIn(List<Status> statuses);

    Optional<UserEntity> findByUsername(String username);
}
