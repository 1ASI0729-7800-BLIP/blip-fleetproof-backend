package com.blip.fleetproof.platform.fleetmanagement.infrastructure.persistence.jpa.entities;

import com.blip.fleetproof.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "resolution_cases") @Getter @Setter @NoArgsConstructor
public class ResolutionCaseEntity extends AuditableAbstractPersistenceEntity {
    @Column(nullable = false) private Long userId;
    @Column(nullable = false) private Long vehicleId;
    private Long reportId;
    @Column(nullable = false) private String findingId;
    @Column(nullable = false) private String title;
    @Column(nullable = false) private String responsible;
    @Column(nullable = false) private String dueDate;
    @Column(nullable = false) private String status;
    @Column(nullable = false, length = 1000) private String evidence = "";
    @Column(nullable = false, length = 2000) private String notes = "";
    @Column(nullable = false) private String occurredAt;
}
