package com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources;

public record CreateAlertResource(
        Long userId,
        Long vehicleId,
        Long reportId,
        String type,
        String status,
        String createdAt,
        String messageKey) {
}
