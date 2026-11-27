package com.blip.fleetproof.platform.fleetmanagement.infrastructure.persistence.jpa.entities;

import com.blip.fleetproof.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "case_activities", indexes = @Index(name = "idx_case_activity_case", columnList = "case_id,recorded_at"))
public class CaseActivityEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "case_id", nullable = false, updatable = false)
    private Long caseId;
    @Column(name = "actor_id", nullable = false, updatable = false)
    private Long actorId;
    @Column(name = "action_type", nullable = false, updatable = false, length = 40)
    private String actionType;
    @Column(nullable = false, updatable = false, length = 1000)
    private String description;
    @Column(nullable = false, updatable = false, length = 150)
    private String responsible;
    @Column(name = "recorded_at", nullable = false, updatable = false)
    private Instant recordedAt;
}
