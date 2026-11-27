package com.blip.fleetproof.platform.fleetmanagement.interfaces.rest;

import com.blip.fleetproof.platform.fleetmanagement.application.internal.ResolutionCaseApplicationService;
import com.blip.fleetproof.platform.fleetmanagement.domain.model.aggregates.ResolutionCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/v1/cases")
public class ResolutionCaseController {
    private final ResolutionCaseApplicationService service;
    private final com.blip.fleetproof.platform.fleetmanagement.application.internal.CaseWorkflowService workflow;
    public ResolutionCaseController(ResolutionCaseApplicationService service,
                                    com.blip.fleetproof.platform.fleetmanagement.application.internal.CaseWorkflowService workflow) {
        this.service = service; this.workflow = workflow;
    }
    @GetMapping public List<CaseResource> all(@RequestParam(required = false) Long userId) { return service.findAll(userId).stream().map(CaseResource::from).toList(); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public CaseResource create(@Valid @RequestBody CaseRequest r) { return CaseResource.from(service.create(r.domain())); }
    @PutMapping("/{id}") public CaseResource update(@PathVariable Long id, @Valid @RequestBody CaseRequest r) { return CaseResource.from(service.update(id, r.domain())); }
    @GetMapping("/{id}/activities")
    public List<com.blip.fleetproof.platform.fleetmanagement.application.internal.CaseWorkflowService.ActivityResource> activities(
            @PathVariable Long id) { return workflow.findAll(id); }
    @PostMapping("/{id}/activities") @ResponseStatus(HttpStatus.CREATED)
    public com.blip.fleetproof.platform.fleetmanagement.application.internal.CaseWorkflowService.ActivityResource addActivity(
            @PathVariable Long id,
            @RequestBody com.blip.fleetproof.platform.fleetmanagement.application.internal.CaseWorkflowService.ActivityRequest request) {
        return workflow.record(id, request);
    }
    public record CaseRequest(@NotNull @Positive Long userId, @NotNull @Positive Long vehicleId, Long reportId,
                              @NotBlank String findingId, @NotBlank String title, @NotBlank String responsible,
                              @NotBlank String dueDate, @NotBlank String status, String evidence, String notes,
                              String createdAt) {
        ResolutionCase domain() { return new ResolutionCase(userId, vehicleId, reportId, findingId, title, responsible, dueDate, status, evidence, notes, createdAt); }
    }
    public record CaseResource(Long id, Long userId, Long vehicleId, Long reportId, String findingId, String title,
                               String responsible, String dueDate, String status, String evidence, String notes,
                               String createdAt) {
        static CaseResource from(ResolutionCase c) { return new CaseResource(c.getId(), c.getUserId(), c.getVehicleId(), c.getReportId(), c.getFindingId(), c.getTitle(), c.getResponsible(), c.getDueDate(), c.getStatus(), c.getEvidence(), c.getNotes(), c.getCreatedAt()); }
    }
}
