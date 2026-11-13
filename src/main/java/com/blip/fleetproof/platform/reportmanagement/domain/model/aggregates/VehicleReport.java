package com.blip.fleetproof.platform.reportmanagement.domain.model.aggregates;

import com.blip.fleetproof.platform.reportmanagement.domain.model.valueobjects.Finding;
import com.blip.fleetproof.platform.reportmanagement.domain.model.valueobjects.SourceCheck;
import com.blip.fleetproof.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;
import java.util.List;

public class VehicleReport extends AbstractDomainAggregateRoot<VehicleReport> {
    private Long id;
    private final Long userId;
    private final Long vehicleId;
    private final String plate;
    private final String createdAt;
    private final Integer version;
    private final List<SourceCheck> sources;

    public VehicleReport(Long userId, Long vehicleId, String plate, String createdAt,
                         Integer version, List<SourceCheck> sources) {
        if (userId == null || userId <= 0 || vehicleId == null || vehicleId <= 0)
            throw new IllegalArgumentException("userId and vehicleId must be positive");
        if (plate == null || plate.isBlank()) throw new IllegalArgumentException("plate is required");
        if (version == null || version <= 0) throw new IllegalArgumentException("version must be positive");
        this.userId = userId; this.vehicleId = vehicleId; this.plate = plate.trim().toUpperCase();
        this.createdAt = createdAt == null || createdAt.isBlank() ? Instant.now().toString() : createdAt;
        this.version = version; this.sources = sources == null ? List.of() : List.copyOf(sources);
    }

    public static VehicleReport reconstitute(Long id, Long userId, Long vehicleId, String plate,
                                              String createdAt, Integer version, List<SourceCheck> sources) {
        var report = new VehicleReport(userId, vehicleId, plate, createdAt, version, sources); report.id = id; return report;
    }

    public List<Finding> findings() { return sources.stream().filter(s -> s.status().equals("available")).flatMap(s -> s.findings().stream()).toList(); }
    public boolean isComplete() { return !sources.isEmpty() && sources.stream().allMatch(s -> s.status().equals("available")); }
    public String risk() {
        if (findings().stream().anyMatch(f -> f.severity().equals("high"))) return "high";
        if (findings().stream().anyMatch(f -> f.severity().equals("medium"))) return "medium";
        return isComplete() ? "low" : "unknown";
    }
    public Long getId() { return id; } public Long getUserId() { return userId; }
    public Long getVehicleId() { return vehicleId; } public String getPlate() { return plate; }
    public String getCreatedAt() { return createdAt; } public Integer getVersion() { return version; }
    public List<SourceCheck> getSources() { return sources; }
}
