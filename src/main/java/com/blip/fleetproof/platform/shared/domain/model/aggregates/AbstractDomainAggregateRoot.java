package com.blip.fleetproof.platform.shared.domain.model.aggregates;

import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.Collection;

/**
 * Base class for FleetProof aggregate roots that publish domain events.
 *
 * @param <T> concrete aggregate root type
 */
public abstract class AbstractDomainAggregateRoot<T extends AbstractDomainAggregateRoot<T>>
        extends AbstractAggregateRoot<T> {

    protected void registerDomainEvent(Object event) {
        super.registerEvent(event);
    }

    @Override
    protected void clearDomainEvents() {
        super.clearDomainEvents();
    }

    @Override
    protected Collection<Object> domainEvents() {
        return super.domainEvents();
    }
}
