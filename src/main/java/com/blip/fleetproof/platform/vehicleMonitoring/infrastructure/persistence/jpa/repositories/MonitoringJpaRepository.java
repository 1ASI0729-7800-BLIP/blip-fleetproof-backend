package com.blip.fleetproof.platform.vehicleMonitoring.infrastructure.persistence.jpa.repositories;

import com.blip.fleetproof.platform.vehicleMonitoring.infrastructure.persistence.jpa.entities.MonitoringPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitoringJpaRepository extends JpaRepository<MonitoringPersistenceEntity, Long> {
    List<MonitoringPersistenceEntity> findAllByUserId(Long userId);

    boolean existsByVehicleId(Long vehicleId);
}
