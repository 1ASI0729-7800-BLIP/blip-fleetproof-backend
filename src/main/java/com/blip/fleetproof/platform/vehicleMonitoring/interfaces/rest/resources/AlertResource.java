package com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources;

/**
 * Matches the frontend AlertData contract; {@code createdAt} is the alert's business time (see Alert#getRaisedAt).
 */
public record AlertResource(
        Long id,
        Long userId,
        Long vehicleId,
        Long reportId,
        String type,
        String status,
        String createdAt,
        String messageKey) {
}
