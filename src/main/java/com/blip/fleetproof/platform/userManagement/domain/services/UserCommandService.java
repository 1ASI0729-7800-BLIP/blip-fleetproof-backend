package com.blip.fleetproof.platform.userManagement.domain.services;

import com.blip.fleetproof.platform.userManagement.domain.model.aggregates.User;
import com.blip.fleetproof.platform.userManagement.domain.model.commands.CreateUserCommand;
import com.blip.fleetproof.platform.userManagement.domain.model.commands.UpdateUserProfileCommand;

public interface UserCommandService {
    User handle(CreateUserCommand command);
    User handle(Long id, UpdateUserProfileCommand command);
}
