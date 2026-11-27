package com.blip.fleetproof.platform.fleetmanagement.application.internal;

import com.blip.fleetproof.platform.shared.domain.exceptions.ConflictException;
import com.blip.fleetproof.platform.fleetmanagement.infrastructure.persistence.jpa.entities.CaseActivityEntity;
import com.blip.fleetproof.platform.fleetmanagement.infrastructure.persistence.jpa.repositories.CaseActivityRepository;
import com.blip.fleetproof.platform.fleetmanagement.infrastructure.persistence.jpa.repositories.ResolutionCaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class CaseWorkflowService {
    private static final Set<String> ACTIONS = Set.of(
            "CREATED", "ASSIGN", "CONTACT_DRIVER", "DRIVER_RESPONDED", "DRIVER_NO_RESPONSE",
            "ESCALATE", "REQUEST_ASSISTANCE", "RECORD_SOLUTION", "RESOLVE", "CLOSE");

    private final ResolutionCaseRepository caseRepository;
    private final CaseActivityRepository activityRepository;

    public CaseWorkflowService(ResolutionCaseRepository caseRepository,
                               CaseActivityRepository activityRepository) {
        this.caseRepository = caseRepository;
        this.activityRepository = activityRepository;
    }

    @Transactional
    public ActivityResource record(Long caseId, ActivityRequest request) {
        var resolutionCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new NoSuchElementException("Case not found"));
        var action = normalizeAction(request.action());
        if ("closed".equals(resolutionCase.getStatus()))
            throw new ConflictException("A closed case cannot receive new activities");

        var description = requireText(request.description(), "description");
        var responsible = request.responsible() == null || request.responsible().isBlank()
                ? resolutionCase.getResponsible() : request.responsible().trim();

        switch (action) {
            case "ASSIGN", "ESCALATE" -> {
                if (request.responsible() == null || request.responsible().isBlank())
                    throw new IllegalArgumentException("responsible is required for assignment or escalation");
                resolutionCase.setResponsible(responsible);
                resolutionCase.setStatus("in-progress");
            }
            case "CONTACT_DRIVER", "DRIVER_RESPONDED", "DRIVER_NO_RESPONSE", "REQUEST_ASSISTANCE" ->
                    resolutionCase.setStatus("in-progress");
            case "RECORD_SOLUTION" -> {
                resolutionCase.setEvidence(description);
                resolutionCase.setStatus("in-progress");
            }
            case "RESOLVE" -> {
                resolutionCase.setEvidence(description);
                resolutionCase.setStatus("resolved");
            }
            case "CLOSE" -> {
                if (!"resolved".equals(resolutionCase.getStatus()))
                    throw new ConflictException("The case must be resolved before it can be closed");
                resolutionCase.setStatus("closed");
            }
            default -> { }
        }
        resolutionCase.setNotes(appendNote(resolutionCase.getNotes(), action + ": " + description));
        caseRepository.save(resolutionCase);
        return resource(saveActivity(caseId, request.actorId(), action, description, responsible));
    }

    @Transactional
    public ActivityResource recordCreated(Long caseId, Long actorId, String responsible, String description) {
        return resource(saveActivity(caseId, actorId, "CREATED", description, responsible));
    }

    @Transactional(readOnly = true)
    public List<ActivityResource> findAll(Long caseId) {
        if (!caseRepository.existsById(caseId)) throw new NoSuchElementException("Case not found");
        return activityRepository.findAllByCaseIdOrderByRecordedAtAsc(caseId).stream().map(this::resource).toList();
    }

    private CaseActivityEntity saveActivity(Long caseId, Long actorId, String action, String description,
                                            String responsible) {
        if (actorId == null || actorId <= 0) throw new IllegalArgumentException("actorId must be positive");
        var entity = new CaseActivityEntity();
        entity.setCaseId(caseId); entity.setActorId(actorId); entity.setActionType(action);
        entity.setDescription(description); entity.setResponsible(responsible); entity.setRecordedAt(Instant.now());
        return activityRepository.save(entity);
    }

    private String normalizeAction(String value) {
        var action = requireText(value, "action").toUpperCase(Locale.ROOT).replace('-', '_');
        if (!ACTIONS.contains(action)) throw new IllegalArgumentException("Unsupported case action");
        return action;
    }

    private String appendNote(String current, String note) {
        var result = current == null || current.isBlank() ? note : current + "\n" + note;
        return result.length() <= 2000 ? result : result.substring(result.length() - 2000);
    }

    private String requireText(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required");
        return value.trim();
    }

    private ActivityResource resource(CaseActivityEntity entity) {
        return new ActivityResource(entity.getId(), entity.getCaseId(), entity.getActorId(), entity.getActionType(),
                entity.getDescription(), entity.getResponsible(), entity.getRecordedAt());
    }

    public record ActivityRequest(Long actorId, String action, String description, String responsible) { }
    public record ActivityResource(Long id, Long caseId, Long actorId, String action, String description,
                                   String responsible, Instant recordedAt) { }
}
