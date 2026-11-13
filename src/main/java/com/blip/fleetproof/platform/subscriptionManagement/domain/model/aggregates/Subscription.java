package com.blip.fleetproof.platform.subscriptionManagement.domain.model.aggregates;

import com.blip.fleetproof.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.commands.CreateSubscriptionCommand;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.commands.UpdateSubscriptionCommand;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.valueobjects.PlanFrequency;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.valueobjects.SubscriptionStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Monitoring plan subscription owned by a user.
 * Persistence-agnostic: JPA mapping lives in the infrastructure layer.
 */
@Getter
public class Subscription extends AbstractDomainAggregateRoot<Subscription> {

    private final Long id;
    private final Long userId;
    private PlanFrequency plan;
    private Integer vehicleLimit;
    private SubscriptionStatus status;
    private BigDecimal amount;

    private Subscription(Long id, Long userId, PlanFrequency plan, Integer vehicleLimit,
                         SubscriptionStatus status, BigDecimal amount) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId, "userId is required");
        applyPlan(plan, vehicleLimit, status, amount);
    }

    public Subscription(CreateSubscriptionCommand command) {
        this(null, command.userId(), command.plan(), command.vehicleLimit(), command.status(), command.amount());
    }

    /** Rebuilds an existing subscription from persisted state. */
    public static Subscription reconstitute(Long id, Long userId, PlanFrequency plan, Integer vehicleLimit,
                                            SubscriptionStatus status, BigDecimal amount) {
        return new Subscription(Objects.requireNonNull(id, "id is required"), userId, plan, vehicleLimit, status, amount);
    }

    public Subscription update(UpdateSubscriptionCommand command) {
        if (command.userId() != null && !command.userId().equals(userId))
            throw new IllegalArgumentException("A subscription cannot be transferred to another user");
        applyPlan(command.plan(), command.vehicleLimit(), command.status(), command.amount());
        return this;
    }

    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE;
    }

    private void applyPlan(PlanFrequency plan, Integer vehicleLimit, SubscriptionStatus status, BigDecimal amount) {
        Objects.requireNonNull(plan, "plan is required");
        if (vehicleLimit == null || vehicleLimit <= 0)
            throw new IllegalArgumentException("vehicleLimit must be greater than zero");
        if (plan.requiresPayment() && (amount == null || amount.signum() <= 0))
            throw new IllegalArgumentException("amount must be greater than zero for paid plans");
        if (amount != null && amount.signum() < 0)
            throw new IllegalArgumentException("amount cannot be negative");
        this.plan = plan;
        this.vehicleLimit = vehicleLimit;
        this.status = Objects.requireNonNullElse(status, SubscriptionStatus.ACTIVE);
        this.amount = amount;
    }
}
