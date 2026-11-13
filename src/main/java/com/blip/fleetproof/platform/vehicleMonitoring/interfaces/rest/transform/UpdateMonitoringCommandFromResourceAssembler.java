package com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.transform;

import com.blip.fleetproof.platform.shared.interfaces.rest.transform.EnumValueParser;
import com.blip.fleetproof.platform.shared.interfaces.rest.transform.IsoInstant;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands.UpdateMonitoringCommand;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.MonitoringStatus;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources.UpdateMonitoringResource;

public final class UpdateMonitoringCommandFromResourceAssembler {

    private UpdateMonitoringCommandFromResourceAssembler() {
    }

    public static UpdateMonitoringCommand toCommandFromResource(Long monitoringId, UpdateMonitoringResource resource) {
        return new UpdateMonitoringCommand(monitoringId, resource.userId(), resource.vehicleId(),
                EnumValueParser.parse(MonitoringStatus.class, resource.status(), "status"),
                IsoInstant.parse(resource.lastCycle(), "lastCycle"));
    }
}
