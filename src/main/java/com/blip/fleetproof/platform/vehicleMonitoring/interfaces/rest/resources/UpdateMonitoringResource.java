package com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources;

/**
 * {@code id} is accepted because the frontend sends the full resource on PUT; the path id is authoritative.
 */
public record UpdateMonitoringResource(
        Long id,
        Long userId,
        Long vehicleId,
        String status,
        String lastCycle) {
}
