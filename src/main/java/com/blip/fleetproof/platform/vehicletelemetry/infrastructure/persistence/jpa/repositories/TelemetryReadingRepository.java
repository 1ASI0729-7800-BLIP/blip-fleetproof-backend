package com.blip.fleetproof.platform.vehicletelemetry.infrastructure.persistence.jpa.repositories;

import com.blip.fleetproof.platform.vehicletelemetry.infrastructure.persistence.jpa.entities.TelemetryReadingEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TelemetryReadingRepository extends JpaRepository<TelemetryReadingEntity, Long> {
    Optional<TelemetryReadingEntity> findTopByVehicleIdOrderByRecordedAtDesc(Long vehicleId);
    List<TelemetryReadingEntity> findAllByVehicleIdOrderByRecordedAtDesc(Long vehicleId, Pageable pageable);
    List<TelemetryReadingEntity> findAllByOrderByRecordedAtDesc(Pageable pageable);
}
