package com.blip.fleetproof.platform.vehicleMonitoring.domain.repositories;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.aggregates.Alert;

import java.util.List;
import java.util.Optional;

/**
 * Domain port for alert persistence.
 */
public interface AlertRepository {
    Alert save(Alert alert);

    Optional<Alert> findById(Long id);

    List<Alert> findAll();

    List<Alert> findAllByUserId(Long userId);
}
