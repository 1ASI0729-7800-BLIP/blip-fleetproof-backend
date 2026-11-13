package com.blip.fleetproof.platform.vehicleMonitoring.infrastructure.persistence.jpa.adapters;

import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.aggregates.Alert;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.repositories.AlertRepository;
import com.blip.fleetproof.platform.vehicleMonitoring.infrastructure.persistence.jpa.entities.AlertPersistenceEntity;
import com.blip.fleetproof.platform.vehicleMonitoring.infrastructure.persistence.jpa.repositories.AlertJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Implements the domain repository port on Spring Data JPA, mapping between aggregate and persistence entity.
 */
@Component
public class AlertRepositoryAdapter implements AlertRepository {

    private final AlertJpaRepository jpaRepository;

    public AlertRepositoryAdapter(AlertJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Alert save(Alert alert) {
        // Load the existing row on update so audit columns (createdAt) are preserved.
        var entity = alert.getId() == null
                ? new AlertPersistenceEntity()
                : jpaRepository.findById(alert.getId()).orElseThrow(() ->
                        new IllegalStateException("Alert %d no longer exists".formatted(alert.getId())));
        if (alert.getId() == null) {
            // Immutable after creation (the columns are not updatable).
            entity.setUserId(alert.getUserId());
            entity.setVehicleId(alert.getVehicleId());
            entity.setReportId(alert.getReportId());
            entity.setType(alert.getType());
            entity.setRaisedAt(alert.getRaisedAt());
            entity.setMessageKey(alert.getMessageKey());
        }
        entity.setStatus(alert.getStatus());
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Alert> findById(Long id) {
        return jpaRepository.findById(id).map(AlertRepositoryAdapter::toDomain);
    }

    @Override
    public List<Alert> findAll() {
        return jpaRepository.findAll().stream().map(AlertRepositoryAdapter::toDomain).toList();
    }

    @Override
    public List<Alert> findAllByUserId(Long userId) {
        return jpaRepository.findAllByUserId(userId).stream().map(AlertRepositoryAdapter::toDomain).toList();
    }

    private static Alert toDomain(AlertPersistenceEntity entity) {
        return Alert.reconstitute(entity.getId(), entity.getUserId(), entity.getVehicleId(), entity.getReportId(),
                entity.getType(), entity.getStatus(), entity.getRaisedAt(), entity.getMessageKey());
    }
}
