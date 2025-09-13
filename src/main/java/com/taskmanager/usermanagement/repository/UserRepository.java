package com.taskmanager.usermanagement.repository;

import com.taskmanager.common.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.taskmanager.usermanagement.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    List<UserEntity> findAllByStatusIn(List<Status> statuses);

    Optional<UserEntity> findByUsername(String username);
}
