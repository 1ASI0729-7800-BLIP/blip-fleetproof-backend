package com.blip.fleetproof.platform.subscriptionManagement.interfaces.rest.resources;

import java.math.BigDecimal;

/**
 * {@code id} is accepted because the frontend sends the full resource on PUT; the path id is authoritative.
 */
public record UpdateSubscriptionResource(
        Long id,
        Long userId,
        String plan,
        Integer vehicleLimit,
        String status,
        BigDecimal amount) {
}
