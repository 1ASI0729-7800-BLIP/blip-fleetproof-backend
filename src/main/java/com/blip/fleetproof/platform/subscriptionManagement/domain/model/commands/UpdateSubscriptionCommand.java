package com.blip.fleetproof.platform.subscriptionManagement.domain.model.commands;

import com.blip.fleetproof.platform.subscriptionManagement.domain.model.valueobjects.PlanFrequency;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.valueobjects.SubscriptionStatus;

import java.math.BigDecimal;

/**
 * @param userId optional; when present it must match the current owner
 * @param status optional; defaults to {@link SubscriptionStatus#ACTIVE}
 */
public record UpdateSubscriptionCommand(
        Long subscriptionId,
        Long userId,
        PlanFrequency plan,
        Integer vehicleLimit,
        SubscriptionStatus status,
        BigDecimal amount) {

    public UpdateSubscriptionCommand {
        if (subscriptionId == null || subscriptionId <= 0)
            throw new IllegalArgumentException("subscriptionId must be a positive number");
        if (plan == null) throw new IllegalArgumentException("plan is required");
        if (status == null) status = SubscriptionStatus.ACTIVE;
    }
}
