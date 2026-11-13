package com.blip.fleetproof.platform.subscriptionManagement.infrastructure.persistence.jpa.entities;

import com.blip.fleetproof.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.valueobjects.PlanFrequency;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.valueobjects.SubscriptionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "subscriptions", indexes = @Index(name = "idx_subscriptions_user_id", columnList = "user_id"))
public class SubscriptionPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PlanFrequency plan;

    @Column(nullable = false)
    private Integer vehicleLimit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubscriptionStatus status;

    /** Null for the demo plan. */
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;
}
