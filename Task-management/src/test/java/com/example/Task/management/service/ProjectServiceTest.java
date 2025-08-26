package com.example.Task.management.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.Task.management.domain.Project;
import com.example.Task.management.dto.ProjectDto;
import com.example.Task.management.repository.ProjectRepository;

public class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private AuditLogService auditLogService;
    private ProjectService projectService;

    @BeforeEach
    void setup() {
        projectRepository = mock(ProjectRepository.class);
        auditLogService = mock(AuditLogService.class);
        projectService = new ProjectService(projectRepository, auditLogService);
    }

    @Test
    void createProject_succeeds() {
        ProjectDto dto = new ProjectDto();
        dto.setName("P1");
        dto.setDescription("Desc");
        dto.setStartDate(LocalDate.now());
        dto.setEndDate(LocalDate.now().plusDays(1));

        when(projectRepository.save(any(Project.class))).thenAnswer(inv -> inv.getArgument(0));

        Project saved = projectService.create(dto);
        assertEquals("P1", saved.getName());
        assertEquals("Desc", saved.getDescription());
        verify(auditLogService).audit(eq("Project"), eq("CREATE"), any());
    }

    @Test
    void updateProject_updatesFields() {
        UUID id = UUID.randomUUID();
        Project p = Project.builder().name("Old").description("Old").build();
        when(projectRepository.findById(id)).thenReturn(Optional.of(p));
        when(projectRepository.save(any(Project.class))).thenAnswer(inv -> inv.getArgument(0));

        ProjectDto dto = new ProjectDto();
        dto.setName("New");
        dto.setDescription("New");

        Project updated = projectService.update(id, dto);
        assertEquals("New", updated.getName());
        assertEquals("New", updated.getDescription());
        verify(auditLogService).audit(eq("Project"), eq("UPDATE"), any());
    }
}
