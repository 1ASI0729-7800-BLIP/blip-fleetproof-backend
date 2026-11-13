package com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.AlertStatus;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.AlertType;

import java.time.Instant;

/**
 * @param status   optional; defaults to {@link AlertStatus#OPEN}
 * @param raisedAt when the alert was raised (the creation time of the report that triggered it)
 */
public record CreateAlertCommand(
        Long userId,
        Long vehicleId,
        Long reportId,
        AlertType type,
        AlertStatus status,
        Instant raisedAt,
        String messageKey) {

    public static final int MESSAGE_KEY_MAX_LENGTH = 100;

    public CreateAlertCommand {
        if (userId == null || userId <= 0) throw new IllegalArgumentException("userId must be a positive number");
        if (vehicleId == null || vehicleId <= 0) throw new IllegalArgumentException("vehicleId must be a positive number");
        if (reportId == null || reportId <= 0) throw new IllegalArgumentException("reportId must be a positive number");
        if (type == null) throw new IllegalArgumentException("type is required");
        if (raisedAt == null) throw new IllegalArgumentException("createdAt is required");
        if (messageKey == null || messageKey.isBlank()) throw new IllegalArgumentException("messageKey is required");
        if (messageKey.length() > MESSAGE_KEY_MAX_LENGTH)
            throw new IllegalArgumentException("messageKey must be at most %d characters".formatted(MESSAGE_KEY_MAX_LENGTH));
        if (status == null) status = AlertStatus.OPEN;
    }
}
