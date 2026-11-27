package com.blip.fleetproof.platform.vehicletelemetry.infrastructure.persistence.jpa.repositories;

import com.blip.fleetproof.platform.vehicletelemetry.infrastructure.persistence.jpa.entities.TelemetryDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TelemetryDeviceRepository extends JpaRepository<TelemetryDeviceEntity, Long> {
    Optional<TelemetryDeviceEntity> findByDeviceIdIgnoreCase(String deviceId);
    List<TelemetryDeviceEntity> findAllByVehicleId(Long vehicleId);
}
