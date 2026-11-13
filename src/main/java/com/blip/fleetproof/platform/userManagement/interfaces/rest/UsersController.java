package com.blip.fleetproof.platform.userManagement.interfaces.rest;

import com.blip.fleetproof.platform.userManagement.domain.model.commands.CreateUserCommand;
import com.blip.fleetproof.platform.userManagement.domain.model.commands.UpdateUserProfileCommand;
import com.blip.fleetproof.platform.userManagement.domain.services.UserQueryService;
import com.blip.fleetproof.platform.userManagement.domain.services.UserCommandService;
import com.blip.fleetproof.platform.userManagement.domain.model.valueobjects.AccountType;
import com.blip.fleetproof.platform.userManagement.interfaces.rest.resources.CreateUserResource;
import com.blip.fleetproof.platform.userManagement.interfaces.rest.resources.UpdateUserResource;
import com.blip.fleetproof.platform.userManagement.interfaces.rest.resources.UserResource;
import com.blip.fleetproof.platform.userManagement.interfaces.rest.transform.UserResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {
    private final UserCommandService commandService;
    private final UserQueryService queryService;
    private final UserResourceAssembler assembler;

    public UsersController(UserCommandService commandService, UserQueryService queryService,
                           UserResourceAssembler assembler) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.assembler = assembler;
    }

    @GetMapping
    public List<UserResource> getUsers(@RequestParam(required = false) String email) {
        return (email == null ? queryService.handle() : queryService.handle(email).stream().toList())
                .stream().map(assembler::toResource).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResource create(@RequestBody CreateUserResource resource) {
        return assembler.toResource(commandService.handle(new CreateUserCommand(
                resource.name(), resource.email(), resource.organization(), parseAccountType(resource.accountType()),
                resource.passwordHash())));
    }

    @PatchMapping("/{id}")
    public UserResource update(@PathVariable Long id, @RequestBody UpdateUserResource resource) {
        return assembler.toResource(commandService.handle(id,
                new UpdateUserProfileCommand(resource.name(), resource.organization())));
    }

    private AccountType parseAccountType(String accountType) {
        if (accountType == null || accountType.isBlank()) return AccountType.PERSONAL;
        try {
            return AccountType.valueOf(accountType.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid accountType", exception);
        }
    }
}
