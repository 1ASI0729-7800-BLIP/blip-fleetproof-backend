package com.blip.fleetproof.platform.reportmanagement.interfaces.rest;

import com.blip.fleetproof.platform.reportmanagement.application.internal.ReportApplicationService;
import com.blip.fleetproof.platform.reportmanagement.domain.model.aggregates.VehicleReport;
import com.blip.fleetproof.platform.reportmanagement.domain.model.valueobjects.Finding;
import com.blip.fleetproof.platform.reportmanagement.domain.model.valueobjects.SourceCheck;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportApplicationService service;
    public ReportController(ReportApplicationService service) { this.service = service; }
    @GetMapping public List<ReportResource> all(@RequestParam(required = false) Long userId, @RequestParam(required = false) Long vehicleId) { return service.findAll(userId, vehicleId).stream().map(ReportResource::from).toList(); }
    @GetMapping("/{id}") public ReportResource one(@PathVariable Long id) { return ReportResource.from(service.findById(id)); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public ReportResource create(@Valid @RequestBody ReportRequest r) { return ReportResource.from(service.create(r.domain())); }

    public record FindingResource(@NotBlank String id, @NotBlank String descriptionKey, @Pattern(regexp = "medium|high") String severity, @NotBlank String source) { Finding domain() { return new Finding(id, descriptionKey, severity, source); } static FindingResource from(Finding f) { return new FindingResource(f.id(), f.descriptionKey(), f.severity(), f.source()); } }
    public record SourceResource(@NotBlank String name, @Pattern(regexp = "available|unavailable") String status, String checkedAt, String evidence, List<@Valid FindingResource> findings) { SourceCheck domain() { return new SourceCheck(name, status, checkedAt, evidence, findings == null ? List.of() : findings.stream().map(FindingResource::domain).toList()); } static SourceResource from(SourceCheck s) { return new SourceResource(s.name(), s.status(), s.checkedAt(), s.evidence(), s.findings().stream().map(FindingResource::from).toList()); } }
    public record ReportRequest(@NotNull @Positive Long userId, @NotNull @Positive Long vehicleId, @NotBlank String plate, String createdAt, @NotNull @Positive Integer version, List<@Valid SourceResource> sources) { VehicleReport domain() { return new VehicleReport(userId, vehicleId, plate, createdAt, version, sources == null ? List.of() : sources.stream().map(SourceResource::domain).toList()); } }
    public record ReportResource(Long id, Long userId, Long vehicleId, String plate, String createdAt, Integer version, List<SourceResource> sources, String risk, boolean complete) { static ReportResource from(VehicleReport r) { return new ReportResource(r.getId(), r.getUserId(), r.getVehicleId(), r.getPlate(), r.getCreatedAt(), r.getVersion(), r.getSources().stream().map(SourceResource::from).toList(), r.risk(), r.isComplete()); } }
}
