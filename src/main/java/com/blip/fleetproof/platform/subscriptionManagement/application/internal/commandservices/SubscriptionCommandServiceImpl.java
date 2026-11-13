package com.blip.fleetproof.platform.subscriptionManagement.application.internal.commandservices;

import com.blip.fleetproof.platform.subscriptionManagement.domain.model.aggregates.Subscription;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.commands.CreateSubscriptionCommand;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.commands.UpdateSubscriptionCommand;
import com.blip.fleetproof.platform.subscriptionManagement.domain.repositories.SubscriptionRepository;
import com.blip.fleetproof.platform.subscriptionManagement.domain.services.SubscriptionCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionCommandServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    @Transactional
    public Subscription handle(CreateSubscriptionCommand command) {
        var subscription = new Subscription(command);
        if (subscription.isActive() && subscriptionRepository.existsActiveByUserId(command.userId()))
            throw new IllegalStateException("User %d already has an active subscription; update it instead"
                    .formatted(command.userId()));
        return subscriptionRepository.save(subscription);
    }

    @Override
    @Transactional
    public Optional<Subscription> handle(UpdateSubscriptionCommand command) {
        return subscriptionRepository.findById(command.subscriptionId())
                .map(subscription -> subscriptionRepository.save(subscription.update(command)));
    }
}
