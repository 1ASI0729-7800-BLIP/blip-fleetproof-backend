package com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetAllMonitoringQuery;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetMonitoringByIdQuery;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetMonitoringByUserIdQuery;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.services.MonitoringCommandService;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.services.MonitoringQueryService;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources.CreateMonitoringResource;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources.MonitoringResource;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources.UpdateMonitoringResource;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.transform.CreateMonitoringCommandFromResourceAssembler;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.transform.MonitoringResourceFromEntityAssembler;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.transform.UpdateMonitoringCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/monitoring", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Monitoring", description = "Continuous vehicle monitoring")
public class MonitoringController {

    private final MonitoringCommandService monitoringCommandService;
    private final MonitoringQueryService monitoringQueryService;

    public MonitoringController(MonitoringCommandService monitoringCommandService,
                                MonitoringQueryService monitoringQueryService) {
        this.monitoringCommandService = monitoringCommandService;
        this.monitoringQueryService = monitoringQueryService;
    }

    @GetMapping
    @Operation(summary = "List monitorings, optionally filtered by userId")
    public List<MonitoringResource> getMonitorings(@RequestParam(required = false) Long userId) {
        var monitorings = userId == null
                ? monitoringQueryService.handle(new GetAllMonitoringQuery())
                : monitoringQueryService.handle(new GetMonitoringByUserIdQuery(userId));
        return monitorings.stream().map(MonitoringResourceFromEntityAssembler::toResourceFromEntity).toList();
    }

    @GetMapping("/{monitoringId}")
    @Operation(summary = "Get a monitoring by id")
    public ResponseEntity<MonitoringResource> getMonitoringById(@PathVariable Long monitoringId) {
        return monitoringQueryService.handle(new GetMonitoringByIdQuery(monitoringId))
                .map(MonitoringResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Start monitoring a vehicle (409 if it is already monitored)")
    public ResponseEntity<MonitoringResource> createMonitoring(@RequestBody CreateMonitoringResource resource) {
        var command = CreateMonitoringCommandFromResourceAssembler.toCommandFromResource(resource);
        var monitoring = monitoringCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MonitoringResourceFromEntityAssembler.toResourceFromEntity(monitoring));
    }

    @PutMapping("/{monitoringId}")
    @Operation(summary = "Pause/resume a monitoring or record a completed cycle")
    public ResponseEntity<MonitoringResource> updateMonitoring(@PathVariable Long monitoringId,
                                                               @RequestBody UpdateMonitoringResource resource) {
        var command = UpdateMonitoringCommandFromResourceAssembler.toCommandFromResource(monitoringId, resource);
        return monitoringCommandService.handle(command)
                .map(MonitoringResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
