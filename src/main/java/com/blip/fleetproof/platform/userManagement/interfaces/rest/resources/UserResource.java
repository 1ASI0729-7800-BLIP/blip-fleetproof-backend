package com.blip.fleetproof.platform.userManagement.interfaces.rest.resources;

public record UserResource(
        Long id,
        String name,
        String email,
        String organization,
        String accountType,
        String passwordHash
) {
}
