package com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.transform;

import com.blip.fleetproof.platform.shared.interfaces.rest.transform.EnumValueParser;
import com.blip.fleetproof.platform.shared.interfaces.rest.transform.IsoInstant;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.aggregates.Alert;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources.AlertResource;

public final class AlertResourceFromEntityAssembler {

    private AlertResourceFromEntityAssembler() {
    }

    public static AlertResource toResourceFromEntity(Alert entity) {
        return new AlertResource(entity.getId(), entity.getUserId(), entity.getVehicleId(), entity.getReportId(),
                EnumValueParser.format(entity.getType()), EnumValueParser.format(entity.getStatus()),
                IsoInstant.format(entity.getRaisedAt()), entity.getMessageKey());
    }
}
