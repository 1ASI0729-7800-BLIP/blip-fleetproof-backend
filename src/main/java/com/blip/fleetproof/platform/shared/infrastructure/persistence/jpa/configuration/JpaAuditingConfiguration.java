package com.blip.fleetproof.platform.shared.infrastructure.persistence.jpa.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Populates createdAt/updatedAt on {@code AuditableAbstractPersistenceEntity} subclasses.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {
}
