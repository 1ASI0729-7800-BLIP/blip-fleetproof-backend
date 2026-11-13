package com.blip.fleetproof.platform.subscriptionManagement.infrastructure.persistence.jpa.adapters;

import com.blip.fleetproof.platform.subscriptionManagement.domain.model.aggregates.Subscription;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.valueobjects.SubscriptionStatus;
import com.blip.fleetproof.platform.subscriptionManagement.domain.repositories.SubscriptionRepository;
import com.blip.fleetproof.platform.subscriptionManagement.infrastructure.persistence.jpa.entities.SubscriptionPersistenceEntity;
import com.blip.fleetproof.platform.subscriptionManagement.infrastructure.persistence.jpa.repositories.SubscriptionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Implements the domain repository port on Spring Data JPA, mapping between aggregate and persistence entity.
 */
@Component
public class SubscriptionRepositoryAdapter implements SubscriptionRepository {

    private final SubscriptionJpaRepository jpaRepository;

    public SubscriptionRepositoryAdapter(SubscriptionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Subscription save(Subscription subscription) {
        // Load the existing row on update so audit columns (createdAt) are preserved.
        var entity = subscription.getId() == null
                ? new SubscriptionPersistenceEntity()
                : jpaRepository.findById(subscription.getId()).orElseThrow(() ->
                        new IllegalStateException("Subscription %d no longer exists".formatted(subscription.getId())));
        if (entity.getUserId() == null) entity.setUserId(subscription.getUserId());
        entity.setPlan(subscription.getPlan());
        entity.setVehicleLimit(subscription.getVehicleLimit());
        entity.setStatus(subscription.getStatus());
        entity.setAmount(subscription.getAmount());
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return jpaRepository.findById(id).map(SubscriptionRepositoryAdapter::toDomain);
    }

    @Override
    public List<Subscription> findAll() {
        return jpaRepository.findAll().stream().map(SubscriptionRepositoryAdapter::toDomain).toList();
    }

    @Override
    public List<Subscription> findAllByUserId(Long userId) {
        return jpaRepository.findAllByUserId(userId).stream().map(SubscriptionRepositoryAdapter::toDomain).toList();
    }

    @Override
    public boolean existsActiveByUserId(Long userId) {
        return jpaRepository.existsByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);
    }

    private static Subscription toDomain(SubscriptionPersistenceEntity entity) {
        return Subscription.reconstitute(entity.getId(), entity.getUserId(), entity.getPlan(),
                entity.getVehicleLimit(), entity.getStatus(), entity.getAmount());
    }
}
