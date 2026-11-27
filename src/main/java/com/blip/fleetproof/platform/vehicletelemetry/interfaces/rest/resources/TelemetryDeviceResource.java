package com.blip.fleetproof.platform.vehicletelemetry.interfaces.rest.resources;

public record TelemetryDeviceResource(
        Long id,
        String deviceId,
        Long userId,
        Long vehicleId,
        String name,
        Boolean active,
        Double minimumTemperatureCelsius,
        Double maximumTemperatureCelsius,
        Double maximumSpeedKph,
        Double minimumBatteryVoltage) {
}
