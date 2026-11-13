package com.blip.fleetproof.platform.subscriptionManagement.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

/**
 * Matches the frontend SubscriptionData contract: plan and status are lowercase strings; amount is omitted for demo.
 */
public record SubscriptionResource(
        Long id,
        Long userId,
        String plan,
        Integer vehicleLimit,
        String status,
        @JsonInclude(JsonInclude.Include.NON_NULL) BigDecimal amount) {
}
