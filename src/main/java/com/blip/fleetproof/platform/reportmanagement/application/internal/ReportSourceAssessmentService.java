package com.blip.fleetproof.platform.reportmanagement.application.internal;

import com.blip.fleetproof.platform.reportmanagement.domain.model.valueobjects.SourceCheck;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Owns the boundary with external vehicle-information providers. Until official
 * provider contracts and credentials are configured, the API reports each
 * source as unavailable instead of fabricating vehicle findings.
 */
@Service
public class ReportSourceAssessmentService {
    private static final List<String> PROVIDERS = List.of("SUNARP", "SAT", "CITV", "SOAT");

    public List<SourceCheck> assess(String plate, Integer version) {
        if (plate == null || plate.isBlank()) throw new IllegalArgumentException("plate is required");
        if (version == null || version <= 0) throw new IllegalArgumentException("version must be positive");
        String checkedAt = Instant.now().toString();
        return PROVIDERS.stream()
                .map(provider -> new SourceCheck(provider, "unavailable", checkedAt,
                        "Official provider integration is not configured", List.of()))
                .toList();
    }
}
