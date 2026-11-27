package com.blip.fleetproof.platform.vehicletelemetry.domain.model.events;

import java.time.Instant;

public record TelemetryReadingReceivedEvent(
        Long readingId,
        Long vehicleId,
        String deviceId,
        Instant recordedAt) {
}
