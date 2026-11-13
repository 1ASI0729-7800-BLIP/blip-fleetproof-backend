package com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.transform;

import com.blip.fleetproof.platform.shared.interfaces.rest.transform.EnumValueParser;
import com.blip.fleetproof.platform.shared.interfaces.rest.transform.IsoInstant;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands.CreateMonitoringCommand;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.MonitoringStatus;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources.CreateMonitoringResource;

public final class CreateMonitoringCommandFromResourceAssembler {

    private CreateMonitoringCommandFromResourceAssembler() {
    }

    public static CreateMonitoringCommand toCommandFromResource(CreateMonitoringResource resource) {
        return new CreateMonitoringCommand(resource.userId(), resource.vehicleId(),
                EnumValueParser.parse(MonitoringStatus.class, resource.status(), "status"),
                IsoInstant.parse(resource.lastCycle(), "lastCycle"));
    }
}
