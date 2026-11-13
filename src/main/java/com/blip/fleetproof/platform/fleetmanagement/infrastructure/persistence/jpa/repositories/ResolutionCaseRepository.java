package com.blip.fleetproof.platform.fleetmanagement.infrastructure.persistence.jpa.repositories;

import com.blip.fleetproof.platform.fleetmanagement.infrastructure.persistence.jpa.entities.ResolutionCaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResolutionCaseRepository extends JpaRepository<ResolutionCaseEntity, Long> { List<ResolutionCaseEntity> findAllByUserId(Long userId); }
