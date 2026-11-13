package com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources;

/**
 * Matches the frontend MonitoringData contract. {@code lastCycle} is always present (null before the first cycle).
 */
public record MonitoringResource(
        Long id,
        Long userId,
        Long vehicleId,
        String status,
        String lastCycle) {
}
