package com.blip.fleetproof.platform.shared.interfaces.rest.transform;

import java.util.Arrays;
import java.util.Locale;

/**
 * Converts between lowercase API values (e.g. "annual") and enum constants.
 */
public final class EnumValueParser {

    private EnumValueParser() {
    }

    /** Returns null for null/blank input so commands can apply their defaults. */
    public static <E extends Enum<E>> E parse(Class<E> type, String value, String field) {
        if (value == null || value.isBlank()) return null;
        try {
            return Enum.valueOf(type, value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            var allowed = Arrays.stream(type.getEnumConstants()).map(EnumValueParser::format).toList();
            throw new IllegalArgumentException("Invalid %s \"%s\". Allowed values: %s".formatted(field, value, allowed));
        }
    }

    public static String format(Enum<?> value) {
        return value == null ? null : value.name().toLowerCase(Locale.ROOT);
    }
}
