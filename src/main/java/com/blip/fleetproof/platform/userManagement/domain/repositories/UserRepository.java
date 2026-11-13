package com.blip.fleetproof.platform.userManagement.domain.repositories;

import com.blip.fleetproof.platform.userManagement.domain.model.aggregates.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
