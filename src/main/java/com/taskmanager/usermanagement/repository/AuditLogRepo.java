package com.taskmanager.usermanagement.repository;

import com.taskmanager.usermanagement.entity.AuditLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepo extends MongoRepository<AuditLogEntity, String> {

}
