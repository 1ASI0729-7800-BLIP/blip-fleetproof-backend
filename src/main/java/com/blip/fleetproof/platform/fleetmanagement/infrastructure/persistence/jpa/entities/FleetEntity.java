package com.blip.fleetproof.platform.fleetmanagement.infrastructure.persistence.jpa.entities;

import com.blip.fleetproof.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "fleets") @Getter @Setter @NoArgsConstructor
public class FleetEntity extends AuditableAbstractPersistenceEntity {
    @Column(nullable = false) private Long userId;
    @Column(nullable = false) private String name;
}
