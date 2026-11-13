package com.blip.fleetproof.platform.subscriptionManagement.infrastructure.persistence.jpa.repositories;

import com.blip.fleetproof.platform.subscriptionManagement.domain.model.valueobjects.SubscriptionStatus;
import com.blip.fleetproof.platform.subscriptionManagement.infrastructure.persistence.jpa.entities.SubscriptionPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionPersistenceEntity, Long> {
    List<SubscriptionPersistenceEntity> findAllByUserId(Long userId);

    boolean existsByUserIdAndStatus(Long userId, SubscriptionStatus status);
}
