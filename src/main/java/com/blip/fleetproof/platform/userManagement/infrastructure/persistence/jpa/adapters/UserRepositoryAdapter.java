package com.blip.fleetproof.platform.userManagement.infrastructure.persistence.jpa.adapters;

import com.blip.fleetproof.platform.userManagement.domain.model.aggregates.User;
import com.blip.fleetproof.platform.userManagement.domain.repositories.UserRepository;
import com.blip.fleetproof.platform.userManagement.infrastructure.persistence.jpa.entities.UserPersistenceEntity;
import com.blip.fleetproof.platform.userManagement.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository repository;

    public UserRepositoryAdapter(UserJpaRepository repository) {
        this.repository = repository;
    }

    public User save(User user) {
        UserPersistenceEntity entity = repository.findById(user.getId() == null ? -1L : user.getId())
                .map(existing -> { existing.updateFrom(user); return existing; })
                .orElseGet(() -> new UserPersistenceEntity(user));
        return repository.save(entity).toDomain();
    }
    public List<User> findAll() { return repository.findAll().stream().map(UserPersistenceEntity::toDomain).toList(); }
    public Optional<User> findById(Long id) { return repository.findById(id).map(UserPersistenceEntity::toDomain); }
    public Optional<User> findByEmail(String email) { return repository.findByEmail(email).map(UserPersistenceEntity::toDomain); }
    public boolean existsByEmail(String email) { return repository.existsByEmail(email.trim().toLowerCase()); }
}
