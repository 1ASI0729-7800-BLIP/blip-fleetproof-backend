package com.blip.fleetproof.platform.reportmanagement.application.internal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportSourceAssessmentServiceTests {
    private final ReportSourceAssessmentService service = new ReportSourceAssessmentService();

    @Test
    void returnsHonestUnavailableProvidersWhenIntegrationsAreNotConfigured() {
        var checks = service.assess("ABC-123", 1);
        assertEquals(4, checks.size());
        assertTrue(checks.stream().allMatch(check -> check.status().equals("unavailable")));
        assertTrue(checks.stream().allMatch(check -> check.findings().isEmpty()));
    }

    @Test
    void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> service.assess(" ", 1));
        assertThrows(IllegalArgumentException.class, () -> service.assess("ABC-123", 0));
    }
}
