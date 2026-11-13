package com.blip.fleetproof.platform.vehicleMonitoring.infrastructure.persistence.jpa.entities;

import com.blip.fleetproof.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.AlertStatus;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.AlertType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "alerts", indexes = @Index(name = "idx_alerts_user_id", columnList = "user_id"))
public class AlertPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "vehicle_id", nullable = false, updatable = false)
    private Long vehicleId;

    @Column(name = "report_id", nullable = false, updatable = false)
    private Long reportId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, length = 20)
    private AlertType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertStatus status;

    /**
     * Business time of the alert (when the triggering report was created). Deliberately not called
     * {@code createdAt}: that column belongs to the audit base class and records when the row was inserted.
     */
    @Column(name = "raised_at", nullable = false, updatable = false)
    private Instant raisedAt;

    @Column(name = "message_key", nullable = false, updatable = false, length = 100)
    private String messageKey;
}
