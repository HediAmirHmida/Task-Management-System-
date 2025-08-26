package com.example.Task.management.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "audit_log")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private java.util.UUID id;

    @Column(name = "entity", nullable = false)
    private String entityName;

    @Column(nullable = false)
    private String action;

    @Column(name = "entity_id")
    private java.util.UUID entityId;

    @Column(name = "details")
    private String detailsJson;

    @Column(name = "performed_by")
    private java.util.UUID userId;

    @Column(name = "performed_at", nullable = false, updatable = false)
    private Instant timestamp = Instant.now();
}


