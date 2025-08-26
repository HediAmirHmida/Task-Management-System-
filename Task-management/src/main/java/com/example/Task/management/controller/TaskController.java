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

import com.example.Task.management.domain.TaskEntity;
import com.example.Task.management.dto.TaskDto;
import com.example.Task.management.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<TaskEntity> list() { return taskService.findAll(); }

    @GetMapping("/{id}")
    public TaskEntity get(@PathVariable UUID id) { return taskService.findById(id); }

    @PostMapping
    public ResponseEntity<TaskEntity> create(@RequestBody TaskDto dto) { return ResponseEntity.ok(taskService.create(dto)); }

    @PutMapping("/{id}")
    public ResponseEntity<TaskEntity> update(@PathVariable UUID id, @RequestBody TaskDto dto) { return ResponseEntity.ok(taskService.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) { taskService.delete(id); return ResponseEntity.noContent().build(); }

    @PostMapping("/{id}/transition/{status}")
    public ResponseEntity<TaskEntity> transition(@PathVariable UUID id, @PathVariable String status) { return ResponseEntity.ok(taskService.transition(id, status)); }
}
