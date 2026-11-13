package com.blip.fleetproof.platform.subscriptionManagement.interfaces.rest.transform;

import com.blip.fleetproof.platform.shared.interfaces.rest.transform.EnumValueParser;

import com.blip.fleetproof.platform.subscriptionManagement.domain.model.commands.CreateSubscriptionCommand;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.valueobjects.PlanFrequency;
import com.blip.fleetproof.platform.subscriptionManagement.domain.model.valueobjects.SubscriptionStatus;
import com.blip.fleetproof.platform.subscriptionManagement.interfaces.rest.resources.CreateSubscriptionResource;

public final class CreateSubscriptionCommandFromResourceAssembler {

    private CreateSubscriptionCommandFromResourceAssembler() {
    }

    public static CreateSubscriptionCommand toCommandFromResource(CreateSubscriptionResource resource) {
        return new CreateSubscriptionCommand(resource.userId(),
                EnumValueParser.parse(PlanFrequency.class, resource.plan(), "plan"),
                resource.vehicleLimit(),
                EnumValueParser.parse(SubscriptionStatus.class, resource.status(), "status"),
                resource.amount());
    }
}
