package com.blip.fleetproof.platform.vehicletelemetry.infrastructure.persistence.jpa.entities;

import com.blip.fleetproof.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "telemetry_devices", indexes = {
        @Index(name = "uk_telemetry_device_external_id", columnList = "device_id", unique = true),
        @Index(name = "idx_telemetry_device_vehicle", columnList = "vehicle_id")
})
public class TelemetryDeviceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "device_id", nullable = false, unique = true, length = 80)
    private String deviceId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;
    @Column(nullable = false, length = 120)
    private String name;
    @Column(nullable = false)
    private Boolean active = true;
    @Column(nullable = false)
    private Double minimumTemperatureCelsius;
    @Column(nullable = false)
    private Double maximumTemperatureCelsius;
    @Column(nullable = false)
    private Double maximumSpeedKph;
    @Column(nullable = false)
    private Double minimumBatteryVoltage;
}
