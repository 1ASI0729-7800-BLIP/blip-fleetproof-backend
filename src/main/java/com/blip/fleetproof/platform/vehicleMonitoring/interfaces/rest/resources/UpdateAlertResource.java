package com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources;

/**
 * {@code id} is accepted because the frontend sends the full resource on PUT; the path id is authoritative.
 */
public record UpdateAlertResource(
        Long id,
        Long userId,
        Long vehicleId,
        Long reportId,
        String type,
        String status,
        String createdAt,
        String messageKey) {
}
