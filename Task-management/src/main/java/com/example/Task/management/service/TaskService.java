package com.example.Task.management.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Task.management.domain.Project;
import com.example.Task.management.domain.TaskEntity;
import com.example.Task.management.domain.TaskPriority;
import com.example.Task.management.domain.TaskStatus;
import com.example.Task.management.domain.User;
import com.example.Task.management.dto.TaskDto;
import com.example.Task.management.repository.ProjectRepository;
import com.example.Task.management.repository.TaskRepository;
import com.example.Task.management.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public List<TaskEntity> findAll() { return taskRepository.findAll(); }
    public TaskEntity findById(UUID id) { return taskRepository.findById(id).orElseThrow(); }

    @Transactional
    public TaskEntity create(TaskDto dto) {
        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow();
        User assignee = dto.getAssigneeId() != null ? userRepository.findById(dto.getAssigneeId()).orElse(null) : null;
        TaskEntity t = TaskEntity.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(TaskStatus.valueOf(dto.getStatus()))
                .priority(TaskPriority.valueOf(dto.getPriority()))
                .project(project)
                .assignee(assignee)
                .build();
        TaskEntity saved = taskRepository.save(t);
        auditLogService.audit("Task", "CREATE", saved.getId());
        return saved;
    }

    @Transactional
    public TaskEntity update(UUID id, TaskDto dto) {
        TaskEntity t = findById(id);
        t.setTitle(dto.getTitle());
        t.setDescription(dto.getDescription());
        if (dto.getStatus() != null) t.setStatus(TaskStatus.valueOf(dto.getStatus()));
        if (dto.getPriority() != null) t.setPriority(TaskPriority.valueOf(dto.getPriority()));
        if (dto.getProjectId() != null) {
            Project p = projectRepository.findById(dto.getProjectId()).orElseThrow();
            t.setProject(p);
        }
        if (dto.getAssigneeId() != null) {
            User u = userRepository.findById(dto.getAssigneeId()).orElseThrow();
            t.setAssignee(u);
        }
        TaskEntity saved = taskRepository.save(t);
        auditLogService.audit("Task", "UPDATE", saved.getId());
        return saved;
    }

    @Transactional
    public void delete(UUID id) {
        taskRepository.deleteById(id);
        auditLogService.audit("Task", "DELETE", id);
    }

    @Transactional
    public TaskEntity transition(UUID id, String nextStatus) {
        TaskEntity t = findById(id);
        TaskStatus current = t.getStatus();
        TaskStatus target = TaskStatus.valueOf(nextStatus);
        // Valid transitions: TODO -> IN_PROGRESS -> DONE
        if (current == TaskStatus.TODO && target == TaskStatus.IN_PROGRESS
                || current == TaskStatus.IN_PROGRESS && target == TaskStatus.DONE) {
            t.setStatus(target);
        } else if (current == target) {
            // no-op
        } else {
            throw new IllegalStateException("Invalid status transition: " + current + " -> " + target);
        }
        TaskEntity saved = taskRepository.save(t);
        auditLogService.audit("Task", "TRANSITION_" + target.name(), saved.getId());
        return saved;
    }
}
