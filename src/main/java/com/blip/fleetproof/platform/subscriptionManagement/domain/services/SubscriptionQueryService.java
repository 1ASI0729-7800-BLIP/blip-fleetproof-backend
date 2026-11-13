package com.blip.fleetproof.platform.subscriptionManagement.domain.services;

import com.blip.fleetproof.platform.subscriptionManagement.domain.model.aggregates.Subscription;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.queries.GetAllSubscriptionsQuery;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.queries.GetSubscriptionByIdQuery;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.queries.GetSubscriptionsByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface SubscriptionQueryService {
    List<Subscription> handle(GetAllSubscriptionsQuery query);

    List<Subscription> handle(GetSubscriptionsByUserIdQuery query);

    Optional<Subscription> handle(GetSubscriptionByIdQuery query);
}
