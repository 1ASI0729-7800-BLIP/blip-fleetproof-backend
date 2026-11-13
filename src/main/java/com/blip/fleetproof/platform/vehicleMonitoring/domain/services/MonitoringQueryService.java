package com.blip.fleetproof.platform.vehicleMonitoring.domain.services;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.aggregates.Monitoring;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetAllMonitoringQuery;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetMonitoringByIdQuery;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetMonitoringByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface MonitoringQueryService {
    List<Monitoring> handle(GetAllMonitoringQuery query);

    List<Monitoring> handle(GetMonitoringByUserIdQuery query);

    Optional<Monitoring> handle(GetMonitoringByIdQuery query);
}
