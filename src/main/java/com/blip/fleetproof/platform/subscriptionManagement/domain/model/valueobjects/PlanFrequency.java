package com.blip.fleetproof.platform.subscriptionManagement.domain.model.valueobjects;

/**
 * Billing frequency of a monitoring plan. {@code DEMO} is the initial plan granted without payment.
 */
public enum PlanFrequency {
    DEMO,
    WEEKLY,
    MONTHLY,
    ANNUAL;

    public boolean requiresPayment() {
        return this != DEMO;
    }
}
