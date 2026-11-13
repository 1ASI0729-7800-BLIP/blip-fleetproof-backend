package com.blip.fleetproof.platform.vehicleMonitoring.domain.model.aggregates;

import com.blip.fleetproof.platform.shared.domain.exceptions.ConflictException;
import com.blip.fleetproof.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands.CreateMonitoringCommand;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands.UpdateMonitoringCommand;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.MonitoringStatus;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

/**
 * Continuous monitoring of one vehicle. The owner and the vehicle never change; only the status
 * and the time of the last completed cycle do. {@code userId} and {@code vehicleId} reference other
 * bounded contexts by id only.
 */
@Getter
public class Monitoring extends AbstractDomainAggregateRoot<Monitoring> {

    private final Long id;
    private final Long userId;
    private final Long vehicleId;
    private MonitoringStatus status;
    private Instant lastCycle;

    private Monitoring(Long id, Long userId, Long vehicleId, MonitoringStatus status, Instant lastCycle) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId, "userId is required");
        this.vehicleId = Objects.requireNonNull(vehicleId, "vehicleId is required");
        this.status = Objects.requireNonNullElse(status, MonitoringStatus.ACTIVE);
        this.lastCycle = lastCycle;
    }

    public Monitoring(CreateMonitoringCommand command) {
        this(null, command.userId(), command.vehicleId(), command.status(), command.lastCycle());
    }

    /** Rebuilds an existing monitoring from persisted state. */
    public static Monitoring reconstitute(Long id, Long userId, Long vehicleId,
                                          MonitoringStatus status, Instant lastCycle) {
        return new Monitoring(Objects.requireNonNull(id, "id is required"), userId, vehicleId, status, lastCycle);
    }

    public boolean isActive() {
        return status == MonitoringStatus.ACTIVE;
    }

    public void pause() {
        status = MonitoringStatus.PAUSED;
    }

    public void resume() {
        status = MonitoringStatus.ACTIVE;
    }

    /** Registers a completed cycle. Only an active monitoring runs cycles. */
    public void recordCycle(Instant completedAt) {
        Objects.requireNonNull(completedAt, "completedAt is required");
        if (!isActive()) throw new ConflictException("A paused monitoring cannot run cycles; resume it first");
        lastCycle = completedAt;
    }

    public Monitoring update(UpdateMonitoringCommand command) {
        if (command.userId() != null && !command.userId().equals(userId))
            throw new IllegalArgumentException("userId of a monitoring cannot be changed");
        if (command.vehicleId() != null && !command.vehicleId().equals(vehicleId))
            throw new IllegalArgumentException("vehicleId of a monitoring cannot be changed");

        if (command.status() == MonitoringStatus.PAUSED) pause();
        else if (command.status() == MonitoringStatus.ACTIVE) resume();

        // A stale client may echo an older lastCycle (e.g. when only toggling the status): never move it backwards.
        if (command.lastCycle() != null && (lastCycle == null || command.lastCycle().isAfter(lastCycle)))
            recordCycle(command.lastCycle());
        return this;
    }
}
