package com.blip.fleetproof.platform.fleetmanagement.domain.model.aggregates;

import com.blip.fleetproof.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.Instant;

public class ResolutionCase extends AbstractDomainAggregateRoot<ResolutionCase> {
    private Long id;
    private final Long userId;
    private final Long vehicleId;
    private final Long reportId;
    private final String findingId;
    private final String title;
    private String responsible;
    private String dueDate;
    private String status;
    private String evidence;
    private String notes;
    private final String createdAt;

    public ResolutionCase(Long userId, Long vehicleId, Long reportId, String findingId, String title,
                          String responsible, String dueDate, String status, String evidence,
                          String notes, String createdAt) {
        if (userId == null || userId <= 0 || vehicleId == null || vehicleId <= 0)
            throw new IllegalArgumentException("userId and vehicleId must be positive");
        this.userId = userId; this.vehicleId = vehicleId; this.reportId = reportId;
        this.findingId = required(findingId, "findingId"); this.title = required(title, "title");
        update(responsible, dueDate, status, evidence, notes);
        this.createdAt = createdAt == null || createdAt.isBlank() ? Instant.now().toString() : createdAt;
    }

    public static ResolutionCase reconstitute(Long id, Long userId, Long vehicleId, Long reportId,
                                               String findingId, String title, String responsible,
                                               String dueDate, String status, String evidence,
                                               String notes, String createdAt) {
        var result = new ResolutionCase(userId, vehicleId, reportId, findingId, title, responsible,
                dueDate, status, evidence, notes, createdAt); result.id = id; return result;
    }

    public void update(String responsible, String dueDate, String status, String evidence, String notes) {
        this.responsible = required(responsible, "responsible");
        this.dueDate = required(dueDate, "dueDate");
        if (!status.matches("open|in-progress|resolved")) throw new IllegalArgumentException("status is invalid");
        if (status.equals("resolved") && (evidence == null || evidence.isBlank()))
            throw new IllegalArgumentException("evidence is required to resolve a case");
        this.status = status; this.evidence = evidence == null ? "" : evidence.trim();
        this.notes = notes == null ? "" : notes.trim();
    }

    private static String required(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required");
        return value.trim();
    }

    public Long getId() { return id; } public Long getUserId() { return userId; }
    public Long getVehicleId() { return vehicleId; } public Long getReportId() { return reportId; }
    public String getFindingId() { return findingId; } public String getTitle() { return title; }
    public String getResponsible() { return responsible; } public String getDueDate() { return dueDate; }
    public String getStatus() { return status; } public String getEvidence() { return evidence; }
    public String getNotes() { return notes; } public String getCreatedAt() { return createdAt; }
}
