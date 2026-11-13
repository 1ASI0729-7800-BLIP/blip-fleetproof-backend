package com.blip.fleetproof.platform.subscriptionManagement.application.internal.queryservices;

import com.blip.fleetproof.platform.subscriptionManagement.domain.model.aggregates.Subscription;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.queries.GetAllSubscriptionsQuery;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.queries.GetSubscriptionByIdQuery;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.queries.GetSubscriptionsByUserIdQuery;
import com.blip.fleetproof.platform.subscriptionManagement.domain.repositories.SubscriptionRepository;
import com.blip.fleetproof.platform.subscriptionManagement.domain.services.SubscriptionQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionQueryServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<Subscription> handle(GetAllSubscriptionsQuery query) {
        return subscriptionRepository.findAll();
    }

    @Override
    public List<Subscription> handle(GetSubscriptionsByUserIdQuery query) {
        return subscriptionRepository.findAllByUserId(query.userId());
    }

    @Override
    public Optional<Subscription> handle(GetSubscriptionByIdQuery query) {
        return subscriptionRepository.findById(query.subscriptionId());
    }
}
