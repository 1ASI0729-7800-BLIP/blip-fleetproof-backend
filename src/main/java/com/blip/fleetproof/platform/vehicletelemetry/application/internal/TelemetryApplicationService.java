package com.blip.fleetproof.platform.vehicletelemetry.application.internal;

import com.blip.fleetproof.platform.fleetmanagement.application.internal.ResolutionCaseApplicationService;
import com.blip.fleetproof.platform.fleetmanagement.domain.model.aggregates.ResolutionCase;
import com.blip.fleetproof.platform.vehicletelemetry.domain.model.events.TelemetryReadingReceivedEvent;
import com.blip.fleetproof.platform.vehicletelemetry.domain.model.events.VehicleRiskDetectedEvent;
import com.blip.fleetproof.platform.vehicletelemetry.domain.model.valueobjects.TelemetryRisk;
import com.blip.fleetproof.platform.vehicletelemetry.infrastructure.persistence.jpa.entities.TelemetryReadingEntity;
import com.blip.fleetproof.platform.vehicletelemetry.infrastructure.persistence.jpa.repositories.TelemetryReadingRepository;
import com.blip.fleetproof.platform.vehicletelemetry.interfaces.rest.resources.CreateTelemetryReadingResource;
import com.blip.fleetproof.platform.vehicletelemetry.interfaces.rest.resources.TelemetryReadingResource;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands.CreateAlertCommand;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.AlertStatus;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.AlertType;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.services.AlertCommandService;
import com.blip.fleetproof.platform.vehicleinformation.application.internal.VehicleApplicationService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TelemetryApplicationService {
    private final TelemetryReadingRepository repository;
    private final TelemetryDeviceApplicationService deviceService;
    private final VehicleApplicationService vehicleService;
    private final AlertCommandService alertService;
    private final ResolutionCaseApplicationService caseService;
    private final com.blip.fleetproof.platform.fleetmanagement.application.internal.CaseWorkflowService caseWorkflow;
    private final ApplicationEventPublisher events;

    public TelemetryApplicationService(TelemetryReadingRepository repository,
                                       TelemetryDeviceApplicationService deviceService,
                                       VehicleApplicationService vehicleService,
                                       AlertCommandService alertService,
                                       ResolutionCaseApplicationService caseService,
                                       com.blip.fleetproof.platform.fleetmanagement.application.internal.CaseWorkflowService caseWorkflow,
                                       ApplicationEventPublisher events) {
        this.repository = repository;
        this.deviceService = deviceService;
        this.vehicleService = vehicleService;
        this.alertService = alertService;
        this.caseService = caseService;
        this.caseWorkflow = caseWorkflow;
        this.events = events;
    }

    @Transactional
    public TelemetryReadingResource create(CreateTelemetryReadingResource resource) {
        var vehicle = vehicleService.findById(resource.vehicleId());
        var device = deviceService.requireActive(resource.deviceId(), resource.vehicleId());
        var previous = repository.findTopByVehicleIdOrderByRecordedAtDesc(resource.vehicleId());
        var risks = evaluate(resource, device);
        var riskCodes = serialize(risks);
        Long alertId = null;
        Long caseId = null;

        if (!risks.isEmpty()) {
            var sameIncident = previous.filter(item -> item.getCritical() && item.getRiskCodes().equals(riskCodes));
            if (sameIncident.isPresent()) {
                alertId = sameIncident.get().getAlertId();
                caseId = sameIncident.get().getResolutionCaseId();
            } else {
                var alert = alertService.handle(new CreateAlertCommand(
                        vehicle.getUserId(), vehicle.getId(), 0L, AlertType.TELEMETRY,
                        AlertStatus.OPEN, resource.recordedAt(), "alerts.telemetryRisk"));
                alertId = alert.getId();

                var resolutionCase = new ResolutionCase(
                        vehicle.getUserId(), vehicle.getId(), 0L,
                        "IOT-" + resource.deviceId() + "-" + resource.recordedAt().toEpochMilli(),
                        titleFor(risks), vehicle.getResponsible(),
                        LocalDate.now(ZoneOffset.UTC).plusDays(1).toString(), "open", "",
                        "Generated automatically from telemetry: " + riskCodes,
                        resource.recordedAt().toString());
                caseId = caseService.create(resolutionCase).getId();
                caseWorkflow.recordCreated(caseId, vehicle.getUserId(), vehicle.getResponsible(),
                        "Incident created automatically from IoT alert #" + alertId + ": " + riskCodes);
            }
        }

        var entity = copy(resource, vehicle.getUserId(), device, risks, alertId, caseId);
        entity = repository.save(entity);
        events.publishEvent(new TelemetryReadingReceivedEvent(
                entity.getId(), entity.getVehicleId(), entity.getDeviceId(), entity.getRecordedAt()));
        if (!risks.isEmpty()) {
            events.publishEvent(new VehicleRiskDetectedEvent(
                    entity.getId(), entity.getVehicleId(), List.copyOf(risks), alertId, caseId, entity.getRecordedAt()));
        }
        return toResource(entity);
    }

    @Transactional(readOnly = true)
    public List<TelemetryReadingResource> findAll(Long vehicleId, int limit) {
        var page = PageRequest.of(0, Math.max(1, Math.min(limit, 200)));
        var readings = vehicleId == null
                ? repository.findAllByOrderByRecordedAtDesc(page)
                : repository.findAllByVehicleIdOrderByRecordedAtDesc(vehicleId, page);
        return readings.stream().map(this::toResource).toList();
    }

    @Transactional(readOnly = true)
    public TelemetryReadingResource findLatest(Long vehicleId) {
        return repository.findTopByVehicleIdOrderByRecordedAtDesc(vehicleId)
                .map(this::toResource)
                .orElseThrow(() -> new NoSuchElementException("No telemetry exists for this vehicle"));
    }

    private List<TelemetryRisk> evaluate(CreateTelemetryReadingResource resource,
                                         com.blip.fleetproof.platform.vehicletelemetry.infrastructure.persistence.jpa.entities.TelemetryDeviceEntity device) {
        var risks = new ArrayList<TelemetryRisk>();
        if (resource.engineTemperatureCelsius() < device.getMinimumTemperatureCelsius() ||
                resource.engineTemperatureCelsius() > device.getMaximumTemperatureCelsius())
            risks.add(TelemetryRisk.TEMPERATURE_OUT_OF_RANGE);
        if (resource.vehicleBatteryVoltage() <= device.getMinimumBatteryVoltage())
            risks.add(TelemetryRisk.LOW_BATTERY);
        if (resource.gpsSpeedKph() > device.getMaximumSpeedKph())
            risks.add(TelemetryRisk.OVERSPEED);
        if (resource.harshMovementDetected())
            risks.add(TelemetryRisk.HARSH_MOVEMENT);
        return risks;
    }

    private TelemetryReadingEntity copy(CreateTelemetryReadingResource source, Long userId,
                                        com.blip.fleetproof.platform.vehicletelemetry.infrastructure.persistence.jpa.entities.TelemetryDeviceEntity device,
                                        List<TelemetryRisk> risks, Long alertId, Long caseId) {
        var entity = new TelemetryReadingEntity();
        entity.setUserId(userId); entity.setVehicleId(source.vehicleId()); entity.setDeviceId(source.deviceId().trim());
        entity.setRecordedAt(source.recordedAt()); entity.setLatitude(source.latitude()); entity.setLongitude(source.longitude());
        entity.setLocationAccuracyMeters(source.locationAccuracyMeters()); entity.setGpsSpeedKph(source.gpsSpeedKph());
        entity.setHeadingDegrees(source.headingDegrees()); entity.setSessionDistanceKm(source.sessionDistanceKm());
        entity.setAccelerationMagnitude(source.accelerationMagnitude()); entity.setHarshMovementDetected(source.harshMovementDetected());
        entity.setEngineTemperatureCelsius(source.engineTemperatureCelsius()); entity.setVehicleBatteryVoltage(source.vehicleBatteryVoltage());
        entity.setEngineRpm(source.engineRpm());
        entity.setMinimumTemperatureThreshold(device.getMinimumTemperatureCelsius());
        entity.setMaximumTemperatureThreshold(device.getMaximumTemperatureCelsius());
        entity.setMaximumSpeedThreshold(device.getMaximumSpeedKph());
        entity.setMinimumBatteryThreshold(device.getMinimumBatteryVoltage());
        entity.setLocationSource(source.locationSource()); entity.setMotionSource(source.motionSource());
        entity.setEngineDataSource(source.engineDataSource()); entity.setCritical(!risks.isEmpty()); entity.setRiskCodes(serialize(risks));
        entity.setAlertId(alertId); entity.setResolutionCaseId(caseId);
        return entity;
    }

    private TelemetryReadingResource toResource(TelemetryReadingEntity entity) {
        return new TelemetryReadingResource(
                entity.getId(), entity.getUserId(), entity.getVehicleId(), entity.getDeviceId(), entity.getRecordedAt(),
                entity.getLatitude(), entity.getLongitude(), entity.getLocationAccuracyMeters(), entity.getGpsSpeedKph(),
                entity.getHeadingDegrees(), entity.getSessionDistanceKm(), entity.getAccelerationMagnitude(),
                entity.getHarshMovementDetected(), entity.getEngineTemperatureCelsius(), entity.getVehicleBatteryVoltage(),
                entity.getEngineRpm(), entity.getMinimumTemperatureThreshold(), entity.getMaximumTemperatureThreshold(),
                entity.getMaximumSpeedThreshold(), entity.getMinimumBatteryThreshold(), entity.getLocationSource(),
                entity.getMotionSource(), entity.getEngineDataSource(),
                entity.getCritical(), deserialize(entity.getRiskCodes()), entity.getAlertId(), entity.getResolutionCaseId());
    }

    private String serialize(List<TelemetryRisk> risks) {
        return risks.stream().map(Enum::name).sorted().reduce((a, b) -> a + "," + b).orElse("");
    }

    private List<String> deserialize(String value) {
        return value == null || value.isBlank() ? List.of() : Arrays.asList(value.split(","));
    }

    private String titleFor(List<TelemetryRisk> risks) {
        if (risks.contains(TelemetryRisk.TEMPERATURE_OUT_OF_RANGE))
            return "Investigate temperature outside configured range";
        if (risks.contains(TelemetryRisk.LOW_BATTERY)) return "Inspect low vehicle battery";
        if (risks.contains(TelemetryRisk.OVERSPEED)) return "Review overspeed incident";
        return "Review harsh vehicle movement";
    }
}
