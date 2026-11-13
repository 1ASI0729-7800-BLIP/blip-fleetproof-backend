package com.blip.fleetproof.platform.userManagement.infrastructure.persistence.jpa.repositories;

import com.blip.fleetproof.platform.userManagement.infrastructure.persistence.jpa.entities.UserPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserPersistenceEntity, Long> {
    Optional<UserPersistenceEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
