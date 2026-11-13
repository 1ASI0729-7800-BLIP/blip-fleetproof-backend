package com.blip.fleetproof.platform.vehicleMonitoring.application.internal.commandservices;

import com.blip.fleetproof.platform.shared.domain.exceptions.ConflictException;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.aggregates.Monitoring;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands.CreateMonitoringCommand;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands.UpdateMonitoringCommand;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.repositories.MonitoringRepository;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.services.MonitoringCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MonitoringCommandServiceImpl implements MonitoringCommandService {

    private final MonitoringRepository monitoringRepository;

    public MonitoringCommandServiceImpl(MonitoringRepository monitoringRepository) {
        this.monitoringRepository = monitoringRepository;
    }

    @Override
    @Transactional
    public Monitoring handle(CreateMonitoringCommand command) {
        if (monitoringRepository.existsByVehicleId(command.vehicleId()))
            throw new ConflictException("Vehicle %d is already being monitored".formatted(command.vehicleId()));
        return monitoringRepository.save(new Monitoring(command));
    }

    @Override
    @Transactional
    public Optional<Monitoring> handle(UpdateMonitoringCommand command) {
        return monitoringRepository.findById(command.monitoringId())
                .map(monitoring -> monitoringRepository.save(monitoring.update(command)));
    }
}
