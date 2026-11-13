package com.blip.fleetproof.platform.userManagement.domain.services;

import com.blip.fleetproof.platform.userManagement.domain.model.aggregates.User;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {
    List<User> handle();
    Optional<User> handle(Long id);
    Optional<User> handle(String email);
}
