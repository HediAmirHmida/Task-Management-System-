package com.example.Task.management.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Task.management.domain.Project;
import com.example.Task.management.dto.ProjectDto;
import com.example.Task.management.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AuditLogService auditLogService;

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project findById(UUID id) {
        return projectRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Project create(ProjectDto dto) {
        Project p = Project.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
        Project saved = projectRepository.save(p);
        auditLogService.audit("Project", "CREATE", saved.getId());
        return saved;
    }

    @Transactional
    public Project update(UUID id, ProjectDto dto) {
        Project p = findById(id);
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setStartDate(dto.getStartDate());
        p.setEndDate(dto.getEndDate());
        Project saved = projectRepository.save(p);
        auditLogService.audit("Project", "UPDATE", saved.getId());
        return saved;
    }

    @Transactional
    public void delete(UUID id) {
        projectRepository.deleteById(id);
        auditLogService.audit("Project", "DELETE", id);
    }
}
