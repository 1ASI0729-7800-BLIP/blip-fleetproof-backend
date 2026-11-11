package com.blip.fleetproof.platform.fleetmanagement.application.internal;

import com.blip.fleetproof.platform.fleetmanagement.domain.model.aggregates.ResolutionCase;
import com.blip.fleetproof.platform.fleetmanagement.infrastructure.persistence.jpa.entities.ResolutionCaseEntity;
import com.blip.fleetproof.platform.fleetmanagement.infrastructure.persistence.jpa.repositories.ResolutionCaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ResolutionCaseApplicationService {
    private final ResolutionCaseRepository repository;
    public ResolutionCaseApplicationService(ResolutionCaseRepository repository) { this.repository = repository; }
    @Transactional(readOnly = true) public List<ResolutionCase> findAll(Long userId) {
        return (userId == null ? repository.findAll() : repository.findAllByUserId(userId)).stream().map(this::domain).toList();
    }
    @Transactional public ResolutionCase create(ResolutionCase value) { return domain(repository.save(copy(value, new ResolutionCaseEntity()))); }
    @Transactional public ResolutionCase update(Long id, ResolutionCase value) {
        var entity = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Case not found"));
        return domain(repository.save(copy(value, entity)));
    }
    private ResolutionCaseEntity copy(ResolutionCase s, ResolutionCaseEntity t) {
        t.setUserId(s.getUserId()); t.setVehicleId(s.getVehicleId()); t.setReportId(s.getReportId());
        t.setFindingId(s.getFindingId()); t.setTitle(s.getTitle()); t.setResponsible(s.getResponsible());
        t.setDueDate(s.getDueDate()); t.setStatus(s.getStatus()); t.setEvidence(s.getEvidence());
        t.setNotes(s.getNotes()); t.setOccurredAt(s.getCreatedAt()); return t;
    }
    private ResolutionCase domain(ResolutionCaseEntity e) {
        return ResolutionCase.reconstitute(e.getId(), e.getUserId(), e.getVehicleId(), e.getReportId(),
                e.getFindingId(), e.getTitle(), e.getResponsible(), e.getDueDate(), e.getStatus(),
                e.getEvidence(), e.getNotes(), e.getOccurredAt());
    }
}
