package com.blip.fleetproof.platform.vehicletelemetry.interfaces.rest;

import com.blip.fleetproof.platform.vehicletelemetry.application.internal.TelemetryApplicationService;
import com.blip.fleetproof.platform.vehicletelemetry.interfaces.rest.resources.CreateTelemetryReadingResource;
import com.blip.fleetproof.platform.vehicletelemetry.interfaces.rest.resources.TelemetryReadingResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Vehicle Telemetry", description = "IoT readings and automatically detected vehicle risks")
public class TelemetryController {
    private final TelemetryApplicationService service;
    private final com.blip.fleetproof.platform.vehicletelemetry.application.internal.TelemetryDeviceApplicationService deviceService;

    public TelemetryController(TelemetryApplicationService service,
                               com.blip.fleetproof.platform.vehicletelemetry.application.internal.TelemetryDeviceApplicationService deviceService) {
        this.service = service;
        this.deviceService = deviceService;
    }

    @PostMapping(value = "/telemetry-devices", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register or update a simulated or physical IoT device and its thresholds")
    public ResponseEntity<com.blip.fleetproof.platform.vehicletelemetry.interfaces.rest.resources.TelemetryDeviceResource> registerDevice(
            @RequestBody com.blip.fleetproof.platform.vehicletelemetry.interfaces.rest.resources.TelemetryDeviceRequest resource) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceService.register(resource));
    }

    @GetMapping("/telemetry-devices")
    @Operation(summary = "List registered IoT devices, optionally filtered by vehicle")
    public List<com.blip.fleetproof.platform.vehicletelemetry.interfaces.rest.resources.TelemetryDeviceResource> findDevices(
            @RequestParam(required = false) Long vehicleId) {
        return deviceService.findAll(vehicleId);
    }

    @PostMapping(value = "/telemetry-readings", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Receive an IoT telemetry reading and trigger the risk process")
    public ResponseEntity<TelemetryReadingResource> create(@RequestBody CreateTelemetryReadingResource resource) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(resource));
    }

    @GetMapping("/telemetry-readings")
    @Operation(summary = "List recent telemetry, optionally filtered by vehicle")
    public List<TelemetryReadingResource> findAll(@RequestParam(required = false) Long vehicleId,
                                                  @RequestParam(defaultValue = "50") int limit) {
        return service.findAll(vehicleId, limit);
    }

    @GetMapping("/vehicles/{vehicleId}/telemetry/latest")
    @Operation(summary = "Get the most recent telemetry reading of a vehicle")
    public TelemetryReadingResource findLatest(@PathVariable Long vehicleId) {
        return service.findLatest(vehicleId);
    }
}
