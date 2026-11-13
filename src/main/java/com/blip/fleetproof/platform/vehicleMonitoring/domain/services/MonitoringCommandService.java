package com.blip.fleetproof.platform.vehicleMonitoring.domain.services;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.aggregates.Monitoring;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands.CreateMonitoringCommand;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands.UpdateMonitoringCommand;

import java.util.Optional;

public interface MonitoringCommandService {
    Monitoring handle(CreateMonitoringCommand command);

    Optional<Monitoring> handle(UpdateMonitoringCommand command);
}
