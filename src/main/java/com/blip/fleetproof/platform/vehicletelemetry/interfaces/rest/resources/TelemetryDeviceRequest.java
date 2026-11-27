package com.blip.fleetproof.platform.vehicletelemetry.interfaces.rest.resources;

public record TelemetryDeviceRequest(
        String deviceId,
        Long vehicleId,
        String name,
        Boolean active,
        Double minimumTemperatureCelsius,
        Double maximumTemperatureCelsius,
        Double maximumSpeedKph,
        Double minimumBatteryVoltage) {

    public TelemetryDeviceRequest {
        if (deviceId == null || deviceId.isBlank()) throw new IllegalArgumentException("deviceId is required");
        if (deviceId.length() > 80) throw new IllegalArgumentException("deviceId must be at most 80 characters");
        if (vehicleId == null || vehicleId <= 0) throw new IllegalArgumentException("vehicleId must be positive");
        if (name == null || name.isBlank()) name = "FleetProof mobile IoT device";
        if (minimumTemperatureCelsius == null || maximumTemperatureCelsius == null ||
                minimumTemperatureCelsius >= maximumTemperatureCelsius)
            throw new IllegalArgumentException("temperature thresholds are invalid");
        if (minimumTemperatureCelsius < -40 || maximumTemperatureCelsius > 250)
            throw new IllegalArgumentException("temperature thresholds are outside the supported range");
        if (maximumSpeedKph == null || maximumSpeedKph <= 0 || maximumSpeedKph > 300)
            throw new IllegalArgumentException("maximumSpeedKph is invalid");
        if (minimumBatteryVoltage == null || minimumBatteryVoltage < 0 || minimumBatteryVoltage > 100)
            throw new IllegalArgumentException("minimumBatteryVoltage is invalid");
        active = active == null || active;
    }
}
