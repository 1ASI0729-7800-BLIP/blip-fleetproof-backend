package com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.MonitoringStatus;

import java.time.Instant;

/**
 * Update of an existing monitoring. The frontend sends the whole resource on every change
 * (pause/resume and "cycle completed"), so the optional fields are interpreted as follows:
 *
 * @param userId    optional echo of the owner; when present it must match
 * @param vehicleId optional echo of the vehicle; when present it must match
 * @param status    optional; null keeps the current status
 * @param lastCycle optional; null, or a value not after the current one, keeps the current value
 */
public record UpdateMonitoringCommand(
        Long monitoringId,
        Long userId,
        Long vehicleId,
        MonitoringStatus status,
        Instant lastCycle) {

    public UpdateMonitoringCommand {
        if (monitoringId == null || monitoringId <= 0)
            throw new IllegalArgumentException("monitoringId must be a positive number");
    }
}
