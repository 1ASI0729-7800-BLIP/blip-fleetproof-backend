package com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects;

public enum AlertType {
    /** A monitoring cycle found new documentary findings. */
    DOCUMENTARY,
    /** A monitoring cycle could not complete all source checks. */
    TECHNICAL
}
