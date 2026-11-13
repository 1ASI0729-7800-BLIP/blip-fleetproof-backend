package com.blip.fleetproof.platform.reportmanagement.infrastructure.persistence.jpa.repositories;

import com.blip.fleetproof.platform.reportmanagement.infrastructure.persistence.jpa.entities.VehicleReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VehicleReportRepository extends JpaRepository<VehicleReportEntity, Long> {
    List<VehicleReportEntity> findAllByUserId(Long userId);
    List<VehicleReportEntity> findAllByVehicleIdOrderByVersionAsc(Long vehicleId);
}
