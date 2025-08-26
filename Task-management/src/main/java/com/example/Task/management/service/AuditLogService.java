package com.example.Task.management.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.Task.management.domain.AuditLog;
import com.example.Task.management.repository.AuditLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void audit(String entity, String action, UUID entityId) {
        AuditLog log = AuditLog.builder()
                .entityName(entity)
                .action(action)
                .entityId(entityId)
                .timestamp(Instant.now())
                .build();
        auditLogRepository.save(log);
    }
}
