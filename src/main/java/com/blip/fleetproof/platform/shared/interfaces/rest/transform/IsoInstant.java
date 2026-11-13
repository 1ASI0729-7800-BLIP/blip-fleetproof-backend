package com.blip.fleetproof.platform.shared.interfaces.rest.transform;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * ISO-8601 instants for the API. Output always carries three fractional digits
 * (e.g. 2026-10-03T14:00:00.000Z), like JavaScript's Date.toISOString(), so the frontend can
 * keep comparing timestamps as plain strings.
 */
public final class IsoInstant {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC);

    private IsoInstant() {
    }

    public static String format(Instant instant) {
        return instant == null ? null : FORMAT.format(instant);
    }

    /** Returns null for null/blank input. */
    public static Instant parse(String value, String field) {
        if (value == null || value.isBlank()) return null;
        try {
            return Instant.parse(value.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid %s \"%s\". Expected an ISO-8601 instant".formatted(field, value));
        }
    }
}
