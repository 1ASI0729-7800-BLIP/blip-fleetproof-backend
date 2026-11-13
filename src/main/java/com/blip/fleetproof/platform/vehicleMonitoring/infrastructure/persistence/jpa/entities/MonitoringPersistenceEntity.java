package com.blip.fleetproof.platform.vehicleMonitoring.infrastructure.persistence.jpa.entities;

import com.blip.fleetproof.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.MonitoringStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "monitoring",
        uniqueConstraints = @UniqueConstraint(name = "uk_monitoring_vehicle_id", columnNames = "vehicle_id"),
        indexes = @Index(name = "idx_monitoring_user_id", columnList = "user_id"))
public class MonitoringPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "vehicle_id", nullable = false, updatable = false)
    private Long vehicleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MonitoringStatus status;

    /** Null until the first cycle completes. */
    @Column(name = "last_cycle")
    private Instant lastCycle;
}
