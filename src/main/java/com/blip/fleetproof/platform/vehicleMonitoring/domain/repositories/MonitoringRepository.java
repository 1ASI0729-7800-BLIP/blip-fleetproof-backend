package com.blip.fleetproof.platform.vehicleMonitoring.domain.repositories;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.aggregates.Monitoring;

import java.util.List;
import java.util.Optional;

/**
 * Domain port for monitoring persistence.
 */
public interface MonitoringRepository {
    Monitoring save(Monitoring monitoring);

    Optional<Monitoring> findById(Long id);

    List<Monitoring> findAll();

    List<Monitoring> findAllByUserId(Long userId);

    boolean existsByVehicleId(Long vehicleId);
}
