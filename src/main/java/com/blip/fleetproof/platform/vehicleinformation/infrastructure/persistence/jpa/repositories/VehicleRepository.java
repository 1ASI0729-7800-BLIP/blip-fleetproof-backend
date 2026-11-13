package com.blip.fleetproof.platform.vehicleinformation.infrastructure.persistence.jpa.repositories;

import com.blip.fleetproof.platform.vehicleinformation.infrastructure.persistence.jpa.entities.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
    List<VehicleEntity> findAllByUserId(Long userId);
    boolean existsByPlateIgnoreCase(String plate);
}
