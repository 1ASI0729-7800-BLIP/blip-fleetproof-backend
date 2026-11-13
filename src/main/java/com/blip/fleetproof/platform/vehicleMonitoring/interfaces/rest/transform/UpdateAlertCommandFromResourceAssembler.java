package com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.transform;

import com.blip.fleetproof.platform.shared.interfaces.rest.transform.EnumValueParser;
import com.blip.fleetproof.platform.shared.interfaces.rest.transform.IsoInstant;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands.UpdateAlertCommand;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.AlertStatus;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.AlertType;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources.UpdateAlertResource;

public final class UpdateAlertCommandFromResourceAssembler {

    private UpdateAlertCommandFromResourceAssembler() {
    }

    public static UpdateAlertCommand toCommandFromResource(Long alertId, UpdateAlertResource resource) {
        return new UpdateAlertCommand(alertId, resource.userId(), resource.vehicleId(), resource.reportId(),
                EnumValueParser.parse(AlertType.class, resource.type(), "type"),
                EnumValueParser.parse(AlertStatus.class, resource.status(), "status"),
                IsoInstant.parse(resource.createdAt(), "createdAt"),
                resource.messageKey());
    }
}
