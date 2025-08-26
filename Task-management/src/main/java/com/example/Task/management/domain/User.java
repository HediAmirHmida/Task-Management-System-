package com.example.Task.management.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private java.util.UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    void preUpdate() {
        this.updatedAt = Instant.now();
    }
    
    // Manual getters/setters to avoid Lombok processing issues
    public java.util.UUID getId() { return id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(UserRole role) { this.role = role; }
    public void setActive(boolean active) { this.active = active; }
    
    // Manual builder methods
    public static UserBuilder builder() {
        return new UserBuilder();
    }
    
    public static class UserBuilder {
        private String name;
        private String email;
        private String passwordHash;
        private UserRole role;
        private boolean active = true;
        
        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }
        
        public UserBuilder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }
        
        public UserBuilder role(UserRole role) {
            this.role = role;
            return this;
        }
        
        public UserBuilder active(boolean active) {
            this.active = active;
            return this;
        }
        
        public User build() {
            User user = new User();
            user.name = this.name;
            user.email = this.email;
            user.passwordHash = this.passwordHash;
            user.role = this.role;
            user.active = this.active;
            return user;
        }
    }
}


