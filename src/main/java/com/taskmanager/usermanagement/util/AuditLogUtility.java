package com.taskmanager.usermanagement.util;

import com.taskmanager.usermanagement.entity.AuditLogEntity;
import com.taskmanager.usermanagement.entity.UserEntity;
import com.taskmanager.usermanagement.repository.AuditLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuditLogUtility {

    private final AuditLogRepo auditLogRepo;

    @Autowired
    public AuditLogUtility(AuditLogRepo auditLogRepo) {
        this.auditLogRepo = auditLogRepo;
    }

    // Add utility methods for audit logging here
    @Transactional
    public void logAction(String action, String userId) {
        AuditLogEntity auditLogEntity = AuditLogEntity.builder()
                .actionType(action)
                .user(userId)
                .timestamp(java.time.LocalDateTime.now())
                .build();
        auditLogRepo.save(auditLogEntity);
    }
}
