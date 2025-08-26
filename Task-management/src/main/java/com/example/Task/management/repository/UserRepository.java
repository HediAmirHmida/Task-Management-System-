package com.example.Task.management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Task.management.domain.User;

public interface UserRepository extends JpaRepository<User, java.util.UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}

