package com.blip.fleetproof.platform.reportmanagement.domain.model.valueobjects;

public record Finding(String id, String descriptionKey, String severity, String source) {
    public Finding {
        if (id == null || id.isBlank() || descriptionKey == null || descriptionKey.isBlank())
            throw new IllegalArgumentException("finding id and descriptionKey are required");
        if (!severity.matches("medium|high")) throw new IllegalArgumentException("finding severity is invalid");
        if (source == null || source.isBlank()) throw new IllegalArgumentException("finding source is required");
    }
}
