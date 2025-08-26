package com.example.Task.management.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Task.management.domain.Project;
import com.example.Task.management.dto.ProjectDto;
import com.example.Task.management.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public List<Project> list() { return projectService.findAll(); }

    @GetMapping("/{id}")
    public Project get(@PathVariable UUID id) { return projectService.findById(id); }

    @PostMapping
    public ResponseEntity<Project> create(@RequestBody ProjectDto dto) { return ResponseEntity.ok(projectService.create(dto)); }

    @PutMapping("/{id}")
    public ResponseEntity<Project> update(@PathVariable UUID id, @RequestBody ProjectDto dto) { return ResponseEntity.ok(projectService.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) { projectService.delete(id); return ResponseEntity.noContent().build(); }
}
