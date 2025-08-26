package com.example.Task.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Task.management.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, java.util.UUID> {
}


