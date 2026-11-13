package com.blip.fleetproof.platform.vehicleMonitoring.domain.model.aggregates;

import com.blip.fleetproof.platform.shared.domain.exceptions.ConflictException;
import com.blip.fleetproof.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands.CreateAlertCommand;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.commands.UpdateAlertCommand;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.AlertStatus;
import com.blip.fleetproof.platform.vehicleMonitoring.domain.model.valueobjects.AlertType;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

/**
 * Notification raised by a monitoring cycle. Immutable except for its review status: an alert is
 * {@code OPEN} until the user acknowledges it, and a reviewed alert never reopens.
 * {@code userId}, {@code vehicleId} and {@code reportId} reference other bounded contexts by id only.
 */
@Getter
public class Alert extends AbstractDomainAggregateRoot<Alert> {

    private final Long id;
    private final Long userId;
    private final Long vehicleId;
    private final Long reportId;
    private final AlertType type;
    private AlertStatus status;
    private final Instant raisedAt;
    private final String messageKey;

    private Alert(Long id, Long userId, Long vehicleId, Long reportId, AlertType type,
                  AlertStatus status, Instant raisedAt, String messageKey) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId, "userId is required");
        this.vehicleId = Objects.requireNonNull(vehicleId, "vehicleId is required");
        this.reportId = Objects.requireNonNull(reportId, "reportId is required");
        this.type = Objects.requireNonNull(type, "type is required");
        this.status = Objects.requireNonNullElse(status, AlertStatus.OPEN);
        this.raisedAt = Objects.requireNonNull(raisedAt, "raisedAt is required");
        this.messageKey = Objects.requireNonNull(messageKey, "messageKey is required");
    }

    public Alert(CreateAlertCommand command) {
        this(null, command.userId(), command.vehicleId(), command.reportId(), command.type(),
                command.status(), command.raisedAt(), command.messageKey());
    }

    /** Rebuilds an existing alert from persisted state. */
    public static Alert reconstitute(Long id, Long userId, Long vehicleId, Long reportId, AlertType type,
                                     AlertStatus status, Instant raisedAt, String messageKey) {
        return new Alert(Objects.requireNonNull(id, "id is required"), userId, vehicleId, reportId, type,
                status, raisedAt, messageKey);
    }

    public boolean isOpen() {
        return status == AlertStatus.OPEN;
    }

    /** Marks the alert as reviewed. Idempotent. */
    public void acknowledge() {
        status = AlertStatus.REVIEWED;
    }

    public Alert update(UpdateAlertCommand command) {
        requireUnchanged("userId", command.userId(), userId);
        requireUnchanged("vehicleId", command.vehicleId(), vehicleId);
        requireUnchanged("reportId", command.reportId(), reportId);
        requireUnchanged("type", command.type(), type);
        requireUnchanged("createdAt", command.raisedAt(), raisedAt);
        requireUnchanged("messageKey", command.messageKey(), messageKey);

        if (command.status() == AlertStatus.REVIEWED) acknowledge();
        else if (command.status() == AlertStatus.OPEN && !isOpen())
            throw new ConflictException("A reviewed alert cannot be reopened");
        return this;
    }

    private static void requireUnchanged(String field, Object requested, Object current) {
        if (requested != null && !requested.equals(current))
            throw new IllegalArgumentException("%s of an alert cannot be changed".formatted(field));
    }
}
