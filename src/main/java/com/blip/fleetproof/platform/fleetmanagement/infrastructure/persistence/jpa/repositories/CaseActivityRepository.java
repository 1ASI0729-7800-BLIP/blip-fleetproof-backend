package com.blip.fleetproof.platform.fleetmanagement.infrastructure.persistence.jpa.repositories;

import com.blip.fleetproof.platform.fleetmanagement.infrastructure.persistence.jpa.entities.CaseActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseActivityRepository extends JpaRepository<CaseActivityEntity, Long> {
    List<CaseActivityEntity> findAllByCaseIdOrderByRecordedAtAsc(Long caseId);
}
