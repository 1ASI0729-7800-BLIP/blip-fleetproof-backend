package com.blip.fleetproof.platform.subscriptionManagement.domain.services;

import com.blip.fleetproof.platform.subscriptionManagement.domain.model.aggregates.Subscription;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.commands.CreateSubscriptionCommand;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.commands.UpdateSubscriptionCommand;

import java.util.Optional;

public interface SubscriptionCommandService {
    Subscription handle(CreateSubscriptionCommand command);

    Optional<Subscription> handle(UpdateSubscriptionCommand command);
}
