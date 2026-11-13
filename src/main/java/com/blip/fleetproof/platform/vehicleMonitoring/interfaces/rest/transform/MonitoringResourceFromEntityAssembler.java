package com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.transform;

import com.blip.fleetproof.platform.shared.interfaces.rest.transform.EnumValueParser;
import com.blip.fleetproof.platform.shared.interfaces.rest.transform.IsoInstant;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.aggregates.Monitoring;
import com.blip.fleetproof.platform.vehicleMonitoring.interfaces.rest.resources.MonitoringResource;

public final class MonitoringResourceFromEntityAssembler {

    private MonitoringResourceFromEntityAssembler() {
    }

    public static MonitoringResource toResourceFromEntity(Monitoring entity) {
        return new MonitoringResource(entity.getId(), entity.getUserId(), entity.getVehicleId(),
                EnumValueParser.format(entity.getStatus()), IsoInstant.format(entity.getLastCycle()));
    }
}
