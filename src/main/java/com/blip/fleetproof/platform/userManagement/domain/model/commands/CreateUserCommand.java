package com.blip.fleetproof.platform.userManagement.domain.model.commands;

import com.blip.fleetproof.platform.userManagement.domain.model.valueobjects.AccountType;

public record CreateUserCommand(
        String name,
        String email,
        String organization,
        AccountType accountType,
        String passwordHash
) {
}
