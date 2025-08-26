package com.example.Task.management.service;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.Task.management.domain.User;
import com.example.Task.management.domain.UserRole;
import com.example.Task.management.dto.UserDto;
import com.example.Task.management.repository.UserRepository;

public class UserServiceTest {

    private UserRepository userRepository;
    private AuditLogService auditLogService;
    private UserService userService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        auditLogService = mock(AuditLogService.class);
        userService = new UserService(userRepository, auditLogService);
    }

    @Test
    void createUser_succeeds() {
        UserDto dto = new UserDto();
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setRole("USER");

        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setActive(true);
            return u;
        });

        User saved = userService.create(dto);
        assertEquals("John", saved.getName());
        assertEquals("john@example.com", saved.getEmail());
        assertEquals(UserRole.USER, saved.getRole());
        verify(auditLogService).audit(eq("User"), eq("CREATE"), any());
    }

    @Test
    void updateUser_updatesFields() {
        UUID id = UUID.randomUUID();
        User existing = User.builder().name("Old").email("old@e.com").role(UserRole.USER).active(true).build();
        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDto dto = new UserDto();
        dto.setName("New");
        dto.setEmail("new@e.com");
        dto.setRole("ADMIN");

        User updated = userService.update(id, dto);
        assertEquals("New", updated.getName());
        assertEquals("new@e.com", updated.getEmail());
        assertEquals(UserRole.ADMIN, updated.getRole());
        verify(auditLogService).audit(eq("User"), eq("UPDATE"), any());
    }

    @Test
    void activate_deactivate_toggleFlag() {
        UUID id = UUID.randomUUID();
        User existing = User.builder().name("U").email("u@e.com").role(UserRole.USER).active(false).build();
        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User activated = userService.activate(id);
        assertTrue(activated.isActive());
        verify(auditLogService).audit(eq("User"), eq("ACTIVATE"), any());

        when(userRepository.findById(id)).thenReturn(Optional.of(activated));
        User deactivated = userService.deactivate(id);
        assertFalse(deactivated.isActive());
        verify(auditLogService).audit(eq("User"), eq("DEACTIVATE"), any());
    }
}
