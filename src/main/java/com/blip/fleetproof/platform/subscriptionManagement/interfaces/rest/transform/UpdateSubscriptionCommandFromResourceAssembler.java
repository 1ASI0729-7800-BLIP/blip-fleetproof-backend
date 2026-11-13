package com.blip.fleetproof.platform.subscriptionManagement.interfaces.rest.transform;

import com.blip.fleetproof.platform.shared.interfaces.rest.transform.EnumValueParser;

import com.blip.fleetproof.platform.subscriptionManagement.domain.model.commands.UpdateSubscriptionCommand;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.valueobjects.PlanFrequency;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.valueobjects.SubscriptionStatus;
import com.blip.fleetproof.platform.subscriptionManagement.interfaces.rest.resources.UpdateSubscriptionResource;

public final class UpdateSubscriptionCommandFromResourceAssembler {

    private UpdateSubscriptionCommandFromResourceAssembler() {
    }

    public static UpdateSubscriptionCommand toCommandFromResource(Long subscriptionId,
                                                                  UpdateSubscriptionResource resource) {
        return new UpdateSubscriptionCommand(subscriptionId, resource.userId(),
                EnumValueParser.parse(PlanFrequency.class, resource.plan(), "plan"),
                resource.vehicleLimit(),
                EnumValueParser.parse(SubscriptionStatus.class, resource.status(), "status"),
                resource.amount());
    }
}
