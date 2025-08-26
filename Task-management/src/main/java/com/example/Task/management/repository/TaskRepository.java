package com.example.Task.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Task.management.domain.TaskEntity;
import com.example.Task.management.domain.TaskStatus;

public interface TaskRepository extends JpaRepository<TaskEntity, java.util.UUID> {
    List<TaskEntity> findByProjectId(java.util.UUID projectId);
    List<TaskEntity> findByAssigneeId(java.util.UUID assigneeId);
    List<TaskEntity> findByStatus(TaskStatus status);
}


