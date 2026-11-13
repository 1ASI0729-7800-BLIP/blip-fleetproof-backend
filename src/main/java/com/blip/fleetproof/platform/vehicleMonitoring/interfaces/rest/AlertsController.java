package com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetAlertByIdQuery;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetAlertsByUserIdQuery;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetAllAlertsQuery;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.services.AlertCommandService;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.services.AlertQueryService;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources.AlertResource;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources.CreateAlertResource;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources.UpdateAlertResource;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.transform.AlertResourceFromEntityAssembler;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.transform.CreateAlertCommandFromResourceAssembler;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.transform.UpdateAlertCommandFromResourceAssembler;
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
@RequestMapping(value = "/api/v1/alerts", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Alerts", description = "Alerts raised by monitoring cycles")
public class AlertsController {

    private final AlertCommandService alertCommandService;
    private final AlertQueryService alertQueryService;

    public AlertsController(AlertCommandService alertCommandService, AlertQueryService alertQueryService) {
        this.alertCommandService = alertCommandService;
        this.alertQueryService = alertQueryService;
    }

    @GetMapping
    @Operation(summary = "List alerts, optionally filtered by userId")
    public List<AlertResource> getAlerts(@RequestParam(required = false) Long userId) {
        var alerts = userId == null
                ? alertQueryService.handle(new GetAllAlertsQuery())
                : alertQueryService.handle(new GetAlertsByUserIdQuery(userId));
        return alerts.stream().map(AlertResourceFromEntityAssembler::toResourceFromEntity).toList();
    }

    @GetMapping("/{alertId}")
    @Operation(summary = "Get an alert by id")
    public ResponseEntity<AlertResource> getAlertById(@PathVariable Long alertId) {
        return alertQueryService.handle(new GetAlertByIdQuery(alertId))
                .map(AlertResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Raise an alert")
    public ResponseEntity<AlertResource> createAlert(@RequestBody CreateAlertResource resource) {
        var command = CreateAlertCommandFromResourceAssembler.toCommandFromResource(resource);
        var alert = alertCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AlertResourceFromEntityAssembler.toResourceFromEntity(alert));
    }

    @PutMapping("/{alertId}")
    @Operation(summary = "Acknowledge an alert (status open -> reviewed)")
    public ResponseEntity<AlertResource> updateAlert(@PathVariable Long alertId,
                                                     @RequestBody UpdateAlertResource resource) {
        var command = UpdateAlertCommandFromResourceAssembler.toCommandFromResource(alertId, resource);
        return alertCommandService.handle(command)
                .map(AlertResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
