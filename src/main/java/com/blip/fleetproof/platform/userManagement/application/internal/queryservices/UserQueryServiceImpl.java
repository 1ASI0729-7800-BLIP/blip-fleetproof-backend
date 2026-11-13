package com.blip.fleetproof.platform.userManagement.application.internal.queryservices;

import com.blip.fleetproof.platform.userManagement.domain.model.aggregates.User;
import com.blip.fleetproof.platform.userManagement.domain.repositories.UserRepository;
import com.blip.fleetproof.platform.userManagement.domain.services.UserQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository repository;

    public UserQueryServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> handle() { return repository.findAll(); }
    public Optional<User> handle(Long id) { return repository.findById(id); }
    public Optional<User> handle(String email) { return repository.findByEmail(email.trim().toLowerCase()); }
}
