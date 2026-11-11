package com.blip.fleetproof.platform.vehicleinformation.infrastructure.persistence.jpa.entities;

import com.blip.fleetproof.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
public class VehicleEntity extends AuditableAbstractPersistenceEntity {
    @Column(nullable = false) private Long userId;
    @Column(nullable = false) private Long fleetId;
    @Column(nullable = false, unique = true, length = 7) private String plate;
    @Column(nullable = false) private String brand;
    @Column(nullable = false) private String model;
    @Column(nullable = false) private Integer year;
    @Column(nullable = false) private String responsible;
    @Column(nullable = false) private String vin = "";
    @Column(nullable = false) private String owner = "";
}
