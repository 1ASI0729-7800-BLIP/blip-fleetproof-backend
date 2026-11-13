package com.blip.fleetproof.platform.vehicleMonitoring.domain.services;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.aggregates.Alert;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetAlertByIdQuery;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetAlertsByUserIdQuery;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetAllAlertsQuery;

import java.util.List;
import java.util.Optional;

public interface AlertQueryService {
    List<Alert> handle(GetAllAlertsQuery query);

    List<Alert> handle(GetAlertsByUserIdQuery query);

    Optional<Alert> handle(GetAlertByIdQuery query);
}
