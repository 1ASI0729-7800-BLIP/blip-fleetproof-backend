package com.blip.fleetproof.platform.vehicleMonitoring.application.internal.queryservices;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.aggregates.Alert;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetAlertByIdQuery;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetAlertsByUserIdQuery;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetAllAlertsQuery;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.repositories.AlertRepository;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.services.AlertQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AlertQueryServiceImpl implements AlertQueryService {

    private final AlertRepository alertRepository;

    public AlertQueryServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public List<Alert> handle(GetAllAlertsQuery query) {
        return alertRepository.findAll();
    }

    @Override
    public List<Alert> handle(GetAlertsByUserIdQuery query) {
        return alertRepository.findAllByUserId(query.userId());
    }

    @Override
    public Optional<Alert> handle(GetAlertByIdQuery query) {
        return alertRepository.findById(query.alertId());
    }
}
