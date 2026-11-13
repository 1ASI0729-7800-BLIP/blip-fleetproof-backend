package com.blip.fleetproof.platform.vehicleMonitoring.application.internal.commandservices;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.aggregates.Alert;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands.CreateAlertCommand;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands.UpdateAlertCommand;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.repositories.AlertRepository;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.services.AlertCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AlertCommandServiceImpl implements AlertCommandService {

    private final AlertRepository alertRepository;

    public AlertCommandServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    @Transactional
    public Alert handle(CreateAlertCommand command) {
        return alertRepository.save(new Alert(command));
    }

    @Override
    @Transactional
    public Optional<Alert> handle(UpdateAlertCommand command) {
        return alertRepository.findById(command.alertId())
                .map(alert -> alertRepository.save(alert.update(command)));
    }
}
