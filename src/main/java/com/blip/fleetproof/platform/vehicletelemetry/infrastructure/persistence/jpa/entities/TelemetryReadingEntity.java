package com.blip.fleetproof.platform.vehicletelemetry.infrastructure.persistence.jpa.entities;

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
@Table(name = "telemetry_readings", indexes = {
        @Index(name = "idx_telemetry_vehicle_recorded", columnList = "vehicle_id,recorded_at"),
        @Index(name = "idx_telemetry_device", columnList = "device_id")
})
public class TelemetryReadingEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "vehicle_id", nullable = false, updatable = false)
    private Long vehicleId;

    @Column(name = "device_id", nullable = false, updatable = false, length = 80)
    private String deviceId;

    @Column(name = "recorded_at", nullable = false, updatable = false)
    private Instant recordedAt;

    private Double latitude;
    private Double longitude;
    private Double locationAccuracyMeters;
    private Double gpsSpeedKph;
    private Double headingDegrees;
    private Double sessionDistanceKm;
    private Double accelerationMagnitude;

    @Column(nullable = false)
    private Boolean harshMovementDetected;

    @Column(nullable = false)
    private Double engineTemperatureCelsius;

    @Column(nullable = false)
    private Double vehicleBatteryVoltage;

    @Column(nullable = false)
    private Integer engineRpm;

    @Column(nullable = false)
    private Double minimumTemperatureThreshold;
    @Column(nullable = false)
    private Double maximumTemperatureThreshold;
    @Column(nullable = false)
    private Double maximumSpeedThreshold;
    @Column(nullable = false)
    private Double minimumBatteryThreshold;

    @Column(nullable = false, length = 30)
    private String locationSource;

    @Column(nullable = false, length = 30)
    private String motionSource;

    @Column(nullable = false, length = 30)
    private String engineDataSource;

    @Column(nullable = false)
    private Boolean critical;

    @Column(nullable = false, length = 250)
    private String riskCodes;

    private Long alertId;
    private Long resolutionCaseId;
}
