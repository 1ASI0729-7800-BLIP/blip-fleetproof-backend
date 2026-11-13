package com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.AlertStatus;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.AlertType;

import java.time.Instant;

/**
 * Update of an existing alert. Only the status can change (acknowledging an alert); the frontend echoes the
 * rest of the resource, and any echoed field that differs from the stored one is rejected rather than ignored.
 *
 * @param status optional; null keeps the current status
 */
public record UpdateAlertCommand(
        Long alertId,
        Long userId,
        Long vehicleId,
        Long reportId,
        AlertType type,
        AlertStatus status,
        Instant raisedAt,
        String messageKey) {

    public UpdateAlertCommand {
        if (alertId == null || alertId <= 0) throw new IllegalArgumentException("alertId must be a positive number");
    }
}
