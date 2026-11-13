package com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources;

public record CreateMonitoringResource(
        Long userId,
        Long vehicleId,
        String status,
        String lastCycle) {
}
