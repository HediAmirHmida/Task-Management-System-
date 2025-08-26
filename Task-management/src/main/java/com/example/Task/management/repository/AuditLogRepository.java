package com.example.Task.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Task.management.domain.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}


