package com.blip.fleetproof.platform.fleetmanagement.domain.model.aggregates;

import com.blip.fleetproof.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

public class Fleet extends AbstractDomainAggregateRoot<Fleet> {
    private Long id;
    private final Long userId;
    private final String name;

    public Fleet(Long userId, String name) {
        if (userId == null || userId <= 0) throw new IllegalArgumentException("userId must be positive");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name is required");
        this.userId = userId;
        this.name = name.trim();
    }

    public static Fleet reconstitute(Long id, Long userId, String name) {
        var fleet = new Fleet(userId, name); fleet.id = id; return fleet;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getName() { return name; }
}
