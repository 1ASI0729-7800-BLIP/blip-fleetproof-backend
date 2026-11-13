package com.blip.fleetproof.platform.userManagement.application.internal.commandservices;

import com.blip.fleetproof.platform.shared.domain.exceptions.ConflictException;
import com.blip.fleetproof.platform.shared.domain.exceptions.ResourceNotFoundException;
import com.blip.fleetproof.platform.userManagement.domain.model.aggregates.User;
import com.blip.fleetproof.platform.userManagement.domain.model.commands.CreateUserCommand;
import com.blip.fleetproof.platform.userManagement.domain.model.commands.UpdateUserProfileCommand;
import com.blip.fleetproof.platform.userManagement.domain.repositories.UserRepository;
import com.blip.fleetproof.platform.userManagement.domain.services.UserCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository repository;

    public UserCommandServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public User handle(CreateUserCommand command) {
        if (repository.existsByEmail(command.email())) {
            throw new ConflictException("auth.emailTaken");
        }
        return repository.save(new User(command.name(), command.email(), command.organization(),
                command.accountType(), command.passwordHash()));
    }

    @Override
    @Transactional
    public User handle(Long id, UpdateUserProfileCommand command) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.updateProfile(command.name(), command.organization());
        return repository.save(user);
    }
}
