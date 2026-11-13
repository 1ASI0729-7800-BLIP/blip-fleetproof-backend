package com.blip.fleetproof.platform.subscriptionManagement.domain.model.commands;

import com.blip.fleetproof.platform.subscriptionManagement.domain.model.valueobjects.PlanFrequency;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.valueobjects.SubscriptionStatus;

import java.math.BigDecimal;

/**
 * @param status optional; defaults to {@link SubscriptionStatus#ACTIVE}
 * @param amount optional only for {@link PlanFrequency#DEMO}
 */
public record CreateSubscriptionCommand(
        Long userId,
        PlanFrequency plan,
        Integer vehicleLimit,
        SubscriptionStatus status,
        BigDecimal amount) {

    public CreateSubscriptionCommand {
        if (userId == null || userId <= 0) throw new IllegalArgumentException("userId must be a positive number");
        if (plan == null) throw new IllegalArgumentException("plan is required");
        if (status == null) status = SubscriptionStatus.ACTIVE;
    }
}
