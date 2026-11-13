package com.blip.fleetproof.platform.userManagement.interfaces.rest.transform;

import com.blip.fleetproof.platform.userManagement.domain.model.aggregates.User;
import com.blip.fleetproof.platform.userManagement.interfaces.rest.resources.UserResource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class UserResourceAssembler {
    public UserResource toResource(User user) {
        return new UserResource(user.getId(), user.getName(), user.getEmail(),
                user.getOrganization(), user.getAccountType().name().toLowerCase(Locale.ROOT),
                user.getPasswordHash());
    }
}
