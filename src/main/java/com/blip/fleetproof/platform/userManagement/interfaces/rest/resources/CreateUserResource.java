package com.blip.fleetproof.platform.userManagement.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserResource(
        @NotBlank String name,
        @NotBlank @Email String email,
        String organization,
        String accountType,
        @NotBlank String passwordHash
) {
}
