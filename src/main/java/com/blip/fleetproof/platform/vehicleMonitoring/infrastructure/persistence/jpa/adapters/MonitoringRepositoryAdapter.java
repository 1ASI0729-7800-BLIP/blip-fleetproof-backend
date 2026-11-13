package com.blip.fleetproof.platform.vehicleMonitoring.infrastructure.persistence.jpa.adapters;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.aggregates.Monitoring;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.repositories.MonitoringRepository;
import com.blip.fleetproof.platform.vehicleMonitoring.infrastructure.persistence.jpa.entities.MonitoringPersistenceEntity;
import com.blip.fleetproof.platform.vehicleMonitoring.infrastructure.persistence.jpa.repositories.MonitoringJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Implements the domain repository port on Spring Data JPA, mapping between aggregate and persistence entity.
 */
@Component
public class MonitoringRepositoryAdapter implements MonitoringRepository {

    private final MonitoringJpaRepository jpaRepository;

    public MonitoringRepositoryAdapter(MonitoringJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Monitoring save(Monitoring monitoring) {
        // Load the existing row on update so audit columns (createdAt) are preserved.
        var entity = monitoring.getId() == null
                ? new MonitoringPersistenceEntity()
                : jpaRepository.findById(monitoring.getId()).orElseThrow(() ->
                        new IllegalStateException("Monitoring %d no longer exists".formatted(monitoring.getId())));
        if (entity.getUserId() == null) entity.setUserId(monitoring.getUserId());
        if (entity.getVehicleId() == null) entity.setVehicleId(monitoring.getVehicleId());
        entity.setStatus(monitoring.getStatus());
        entity.setLastCycle(monitoring.getLastCycle());
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Monitoring> findById(Long id) {
        return jpaRepository.findById(id).map(MonitoringRepositoryAdapter::toDomain);
    }

    @Override
    public List<Monitoring> findAll() {
        return jpaRepository.findAll().stream().map(MonitoringRepositoryAdapter::toDomain).toList();
    }

    @Override
    public List<Monitoring> findAllByUserId(Long userId) {
        return jpaRepository.findAllByUserId(userId).stream().map(MonitoringRepositoryAdapter::toDomain).toList();
    }

    @Override
    public boolean existsByVehicleId(Long vehicleId) {
        return jpaRepository.existsByVehicleId(vehicleId);
    }

    private static Monitoring toDomain(MonitoringPersistenceEntity entity) {
        return Monitoring.reconstitute(entity.getId(), entity.getUserId(), entity.getVehicleId(),
                entity.getStatus(), entity.getLastCycle());
    }
}
