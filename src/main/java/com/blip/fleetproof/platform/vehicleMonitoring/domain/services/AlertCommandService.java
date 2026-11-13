package com.blip.fleetproof.platform.vehicleMonitoring.domain.services;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.aggregates.Alert;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands.CreateAlertCommand;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands.UpdateAlertCommand;

import java.util.Optional;

public interface AlertCommandService {
    Alert handle(CreateAlertCommand command);

    Optional<Alert> handle(UpdateAlertCommand command);
}
