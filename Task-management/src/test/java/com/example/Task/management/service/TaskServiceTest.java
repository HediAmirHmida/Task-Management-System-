package com.example.Task.management.service;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.Task.management.domain.Project;
import com.example.Task.management.domain.TaskEntity;
import com.example.Task.management.domain.TaskPriority;
import com.example.Task.management.domain.TaskStatus;
import com.example.Task.management.dto.TaskDto;
import com.example.Task.management.repository.ProjectRepository;
import com.example.Task.management.repository.TaskRepository;
import com.example.Task.management.repository.UserRepository;

public class TaskServiceTest {

    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private AuditLogService auditLogService;
    private TaskService taskService;

    @BeforeEach
    void setup() {
        taskRepository = mock(TaskRepository.class);
        projectRepository = mock(ProjectRepository.class);
        userRepository = mock(UserRepository.class);
        auditLogService = mock(AuditLogService.class);
        taskService = new TaskService(taskRepository, projectRepository, userRepository, auditLogService);
    }

    @Test
    void createTask_succeeds() {
        UUID projectId = UUID.randomUUID();
        Project p = Project.builder().name("P").build();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(p));
        when(taskRepository.save(any(TaskEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskDto dto = new TaskDto();
        dto.setProjectId(projectId);
        dto.setTitle("T");
        dto.setDescription("D");
        dto.setStatus(TaskStatus.TODO.name());
        dto.setPriority(TaskPriority.MEDIUM.name());

        TaskEntity saved = taskService.create(dto);
        assertEquals("T", saved.getTitle());
        assertEquals(TaskStatus.TODO, saved.getStatus());
        verify(auditLogService).audit(eq("Task"), eq("CREATE"), any());
    }

    @Test
    void transition_enforcesWorkflow() {
        UUID id = UUID.randomUUID();
        TaskEntity t = TaskEntity.builder().title("T").status(TaskStatus.TODO).build();
        when(taskRepository.findById(id)).thenReturn(Optional.of(t));
        when(taskRepository.save(any(TaskEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskEntity inProgress = taskService.transition(id, TaskStatus.IN_PROGRESS.name());
        assertEquals(TaskStatus.IN_PROGRESS, inProgress.getStatus());

        TaskEntity done = taskService.transition(id, TaskStatus.DONE.name());
        assertEquals(TaskStatus.DONE, done.getStatus());

        assertThrows(IllegalStateException.class, () -> taskService.transition(id, TaskStatus.TODO.name()));
    }
}
