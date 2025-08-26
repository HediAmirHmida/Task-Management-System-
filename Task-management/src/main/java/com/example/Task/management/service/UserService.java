package com.example.Task.management.service;

import com.example.Task.management.domain.User;
import com.example.Task.management.domain.UserRole;
import com.example.Task.management.dto.UserDto;
import com.example.Task.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Transactional
    public User create(UserDto dto) {
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .passwordHash("changeme")
                .role(UserRole.valueOf(dto.getRole()))
                .active(true)
                .build();
        User saved = userRepository.save(user);
        auditLogService.audit("User", "CREATE", saved.getId());
        return saved;
    }

    @Transactional
    public User update(UUID id, UserDto dto) {
        User user = findById(id);
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(UserRole.valueOf(dto.getRole()));
        User saved = userRepository.save(user);
        auditLogService.audit("User", "UPDATE", saved.getId());
        return saved;
    }

    @Transactional
    public void delete(UUID id) {
        userRepository.deleteById(id);
        auditLogService.audit("User", "DELETE", id);
    }

    @Transactional
    public User activate(UUID id) {
        User user = findById(id);
        user.setActive(true);
        User saved = userRepository.save(user);
        auditLogService.audit("User", "ACTIVATE", saved.getId());
        return saved;
    }

    @Transactional
    public User deactivate(UUID id) {
        User user = findById(id);
        user.setActive(false);
        User saved = userRepository.save(user);
        auditLogService.audit("User", "DEACTIVATE", saved.getId());
        return saved;
    }
}
