package com.blip.fleetproof.platform.reportmanagement.domain.model.valueobjects;

import java.util.List;

public record SourceCheck(String name, String status, String checkedAt, String evidence, List<Finding> findings) {
    public SourceCheck {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("source name is required");
        if (!status.matches("available|unavailable")) throw new IllegalArgumentException("source status is invalid");
        findings = findings == null ? List.of() : List.copyOf(findings);
        evidence = evidence == null ? "" : evidence;
    }
}
