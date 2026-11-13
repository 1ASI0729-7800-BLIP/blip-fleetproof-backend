package com.blip.fleetproof.platform.reportmanagement.infrastructure.persistence.jpa.entities;

import com.blip.fleetproof.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "vehicle_reports") @Getter @Setter @NoArgsConstructor
public class VehicleReportEntity extends AuditableAbstractPersistenceEntity {
    @Column(nullable = false) private Long userId;
    @Column(nullable = false) private Long vehicleId;
    @Column(nullable = false) private String plate;
    @Column(nullable = false) private String occurredAt;
    @Column(nullable = false) private Integer version;
    @Lob
    @Column(nullable = false)
    private String sourcesJson = "[]";
}
