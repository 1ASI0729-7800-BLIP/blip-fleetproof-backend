package com.blip.fleetproof.platform.vehicletelemetry.interfaces.rest.resources;

import java.time.Instant;
import java.util.List;

public record TelemetryReadingResource(
        Long id,
        Long userId,
        Long vehicleId,
        String deviceId,
        Instant recordedAt,
        Double latitude,
        Double longitude,
        Double locationAccuracyMeters,
        Double gpsSpeedKph,
        Double headingDegrees,
        Double sessionDistanceKm,
        Double accelerationMagnitude,
        Boolean harshMovementDetected,
        Double engineTemperatureCelsius,
        Double vehicleBatteryVoltage,
        Integer engineRpm,
        Double minimumTemperatureThreshold,
        Double maximumTemperatureThreshold,
        Double maximumSpeedThreshold,
        Double minimumBatteryThreshold,
        String locationSource,
        String motionSource,
        String engineDataSource,
        Boolean critical,
        List<String> risks,
        Long alertId,
        Long resolutionCaseId) {
}
