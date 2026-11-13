package com.blip.fleetproof.platform.vehicleMonitoring.infrastructure.persistence.jpa.repositories;

import com.blip.fleetproof.platform.vehicleMonitoring.infrastructure.persistence.jpa.entities.AlertPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertJpaRepository extends JpaRepository<AlertPersistenceEntity, Long> {
    List<AlertPersistenceEntity> findAllByUserId(Long userId);
}
