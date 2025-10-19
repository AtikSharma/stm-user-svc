package com.taskmanager.usermanagement.repository;

import com.taskmanager.usermanagement.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepo extends JpaRepository<AuditLogEntity, String> {

}
