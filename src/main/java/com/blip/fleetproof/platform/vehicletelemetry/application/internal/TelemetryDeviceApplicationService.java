package com.blip.fleetproof.platform.vehicletelemetry.application.internal;

import com.blip.fleetproof.platform.shared.domain.exceptions.ConflictException;
import com.blip.fleetproof.platform.vehicletelemetry.infrastructure.persistence.jpa.entities.TelemetryDeviceEntity;
import com.blip.fleetproof.platform.vehicletelemetry.infrastructure.persistence.jpa.repositories.TelemetryDeviceRepository;
import com.blip.fleetproof.platform.vehicletelemetry.interfaces.rest.resources.TelemetryDeviceRequest;
import com.blip.fleetproof.platform.vehicletelemetry.interfaces.rest.resources.TelemetryDeviceResource;
import com.blip.fleetproof.platform.vehicleinformation.application.internal.VehicleApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TelemetryDeviceApplicationService {
    private final TelemetryDeviceRepository repository;
    private final VehicleApplicationService vehicleService;

    public TelemetryDeviceApplicationService(TelemetryDeviceRepository repository,
                                             VehicleApplicationService vehicleService) {
        this.repository = repository;
        this.vehicleService = vehicleService;
    }

    @Transactional
    public TelemetryDeviceResource register(TelemetryDeviceRequest request) {
        var vehicle = vehicleService.findById(request.vehicleId());
        var entity = repository.findByDeviceIdIgnoreCase(request.deviceId()).orElseGet(TelemetryDeviceEntity::new);
        if (entity.getId() != null && !entity.getVehicleId().equals(vehicle.getId()))
            throw new ConflictException("The IoT device is already assigned to another vehicle");
        entity.setDeviceId(request.deviceId().trim().toUpperCase());
        entity.setUserId(vehicle.getUserId());
        entity.setVehicleId(vehicle.getId());
        entity.setName(request.name().trim());
        entity.setActive(request.active());
        entity.setMinimumTemperatureCelsius(request.minimumTemperatureCelsius());
        entity.setMaximumTemperatureCelsius(request.maximumTemperatureCelsius());
        entity.setMaximumSpeedKph(request.maximumSpeedKph());
        entity.setMinimumBatteryVoltage(request.minimumBatteryVoltage());
        return resource(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<TelemetryDeviceResource> findAll(Long vehicleId) {
        return (vehicleId == null ? repository.findAll() : repository.findAllByVehicleId(vehicleId))
                .stream().map(this::resource).toList();
    }

    @Transactional(readOnly = true)
    public TelemetryDeviceEntity requireActive(String deviceId, Long vehicleId) {
        var device = repository.findByDeviceIdIgnoreCase(deviceId)
                .orElseThrow(() -> new NoSuchElementException("IoT device is not registered"));
        if (!device.getVehicleId().equals(vehicleId))
            throw new ConflictException("IoT device is not assigned to the requested vehicle");
        if (!device.getActive()) throw new ConflictException("IoT device is inactive");
        return device;
    }

    private TelemetryDeviceResource resource(TelemetryDeviceEntity entity) {
        return new TelemetryDeviceResource(entity.getId(), entity.getDeviceId(), entity.getUserId(),
                entity.getVehicleId(), entity.getName(), entity.getActive(),
                entity.getMinimumTemperatureCelsius(), entity.getMaximumTemperatureCelsius(),
                entity.getMaximumSpeedKph(), entity.getMinimumBatteryVoltage());
    }
}
