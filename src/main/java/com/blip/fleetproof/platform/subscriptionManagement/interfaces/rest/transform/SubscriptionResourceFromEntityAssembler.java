package com.blip.fleetproof.platform.subscriptionManagement.interfaces.rest.transform;

import com.blip.fleetproof.platform.shared.interfaces.rest.transform.EnumValueParser;

import com.blip.fleetproof.platform.subscriptionManagement.domain.model.aggregates.Subscription;
import com.blip.fleetproof.platform.subscriptionManagement.interfaces.rest.resources.SubscriptionResource;

public final class SubscriptionResourceFromEntityAssembler {

    private SubscriptionResourceFromEntityAssembler() {
    }

    public static SubscriptionResource toResourceFromEntity(Subscription entity) {
        return new SubscriptionResource(entity.getId(), entity.getUserId(),
                EnumValueParser.format(entity.getPlan()), entity.getVehicleLimit(),
                EnumValueParser.format(entity.getStatus()), entity.getAmount());
    }
}
