package com.blip.fleetproof.platform.vehicletelemetry.domain.model.events;

import com.blip.fleetproof.platform.vehicletelemetry.domain.model.valueobjects.TelemetryRisk;

import java.time.Instant;
import java.util.List;

public record VehicleRiskDetectedEvent(
        Long readingId,
        Long vehicleId,
        List<TelemetryRisk> risks,
        Long alertId,
        Long resolutionCaseId,
        Instant detectedAt) {
}
