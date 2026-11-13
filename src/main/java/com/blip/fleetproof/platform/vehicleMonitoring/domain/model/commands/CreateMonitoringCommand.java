package com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.MonitoringStatus;

import java.time.Instant;

/**
 * @param status    optional; defaults to {@link MonitoringStatus#ACTIVE}
 * @param lastCycle optional; null means the vehicle has never been checked
 */
public record CreateMonitoringCommand(
        Long userId,
        Long vehicleId,
        MonitoringStatus status,
        Instant lastCycle) {

    public CreateMonitoringCommand {
        if (userId == null || userId <= 0) throw new IllegalArgumentException("userId must be a positive number");
        if (vehicleId == null || vehicleId <= 0) throw new IllegalArgumentException("vehicleId must be a positive number");
        if (status == null) status = MonitoringStatus.ACTIVE;
    }
}
