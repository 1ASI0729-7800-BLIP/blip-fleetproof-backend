package com.blip.fleetproof.platform.vehicletelemetry.interfaces.rest.resources;

import java.time.Instant;
import java.util.Set;

public record CreateTelemetryReadingResource(
        String deviceId,
        Long vehicleId,
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
        String locationSource,
        String motionSource,
        String engineDataSource) {

    private static final Set<String> SOURCES = Set.of("DEVICE_SENSOR", "SIMULATED");

    public CreateTelemetryReadingResource {
        if (deviceId == null || deviceId.isBlank()) throw new IllegalArgumentException("deviceId is required");
        if (deviceId.length() > 80) throw new IllegalArgumentException("deviceId must be at most 80 characters");
        if (vehicleId == null || vehicleId <= 0) throw new IllegalArgumentException("vehicleId must be positive");
        if (recordedAt == null) recordedAt = Instant.now();
        gpsSpeedKph = nonNegative(gpsSpeedKph, "gpsSpeedKph");
        sessionDistanceKm = nonNegative(sessionDistanceKm, "sessionDistanceKm");
        accelerationMagnitude = nonNegative(accelerationMagnitude, "accelerationMagnitude");
        if (engineTemperatureCelsius == null || engineTemperatureCelsius < -40 || engineTemperatureCelsius > 250)
            throw new IllegalArgumentException("engineTemperatureCelsius is outside the supported range");
        if (vehicleBatteryVoltage == null || vehicleBatteryVoltage < 0 || vehicleBatteryVoltage > 100)
            throw new IllegalArgumentException("vehicleBatteryVoltage is outside the supported range");
        if (engineRpm == null || engineRpm < 0 || engineRpm > 15000)
            throw new IllegalArgumentException("engineRpm is outside the supported range");
        locationSource = requireSource(locationSource, "locationSource");
        motionSource = requireSource(motionSource, "motionSource");
        engineDataSource = requireSource(engineDataSource, "engineDataSource");
        harshMovementDetected = Boolean.TRUE.equals(harshMovementDetected);
    }

    private static Double nonNegative(Double value, String field) {
        if (value == null) return 0.0;
        if (value < 0) throw new IllegalArgumentException(field + " must not be negative");
        return value;
    }

    private static String requireSource(String value, String field) {
        if (!SOURCES.contains(value)) throw new IllegalArgumentException(field + " is invalid");
        return value;
    }
}
