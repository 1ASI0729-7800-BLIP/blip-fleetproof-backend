package com.blip.fleetproof.platform.subscriptionManagement.interfaces.rest.resources;

import java.math.BigDecimal;

public record CreateSubscriptionResource(
        Long userId,
        String plan,
        Integer vehicleLimit,
        String status,
        BigDecimal amount) {
}
