package com.blip.fleetproof.platform.vehicleMonitoring.application.internal.queryservices;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.aggregates.Monitoring;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetAllMonitoringQuery;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetMonitoringByIdQuery;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.queries.GetMonitoringByUserIdQuery;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.repositories.MonitoringRepository;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.services.MonitoringQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MonitoringQueryServiceImpl implements MonitoringQueryService {

    private final MonitoringRepository monitoringRepository;

    public MonitoringQueryServiceImpl(MonitoringRepository monitoringRepository) {
        this.monitoringRepository = monitoringRepository;
    }

    @Override
    public List<Monitoring> handle(GetAllMonitoringQuery query) {
        return monitoringRepository.findAll();
    }

    @Override
    public List<Monitoring> handle(GetMonitoringByUserIdQuery query) {
        return monitoringRepository.findAllByUserId(query.userId());
    }

    @Override
    public Optional<Monitoring> handle(GetMonitoringByIdQuery query) {
        return monitoringRepository.findById(query.monitoringId());
    }
}
