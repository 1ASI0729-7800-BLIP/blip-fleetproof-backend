package com.blip.fleetproof.platform.subscriptionManagement.domain.repositories;

import com.blip.fleetproof.platform.subscriptionManagement.domain.model.aggregates.Subscription;

import java.util.List;
import java.util.Optional;

/**
 * Domain port for subscription persistence.
 */
public interface SubscriptionRepository {
    Subscription save(Subscription subscription);

    Optional<Subscription> findById(Long id);

    List<Subscription> findAll();

    List<Subscription> findAllByUserId(Long userId);

    boolean existsActiveByUserId(Long userId);
}
