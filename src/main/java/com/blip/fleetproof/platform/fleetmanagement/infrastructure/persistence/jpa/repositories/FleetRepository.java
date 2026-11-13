package com.blip.fleetproof.platform.fleetmanagement.infrastructure.persistence.jpa.repositories;

import com.blip.fleetproof.platform.fleetmanagement.infrastructure.persistence.jpa.entities.FleetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FleetRepository extends JpaRepository<FleetEntity, Long> { List<FleetEntity> findAllByUserId(Long userId); }
