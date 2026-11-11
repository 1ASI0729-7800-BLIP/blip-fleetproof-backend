package com.blip.fleetproof.platform.fleetmanagement.interfaces.rest;

import com.blip.fleetproof.platform.fleetmanagement.application.internal.FleetApplicationService;
import com.blip.fleetproof.platform.fleetmanagement.domain.model.aggregates.Fleet;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/v1/fleets")
public class FleetController {
    private final FleetApplicationService service;
    public FleetController(FleetApplicationService service) { this.service = service; }
    @GetMapping public List<FleetResource> all(@RequestParam(required = false) Long userId) { return service.findAll(userId).stream().map(FleetResource::from).toList(); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public FleetResource create(@Valid @RequestBody FleetRequest r) { return FleetResource.from(service.create(new Fleet(r.userId(), r.name()))); }
    public record FleetRequest(@NotNull @Positive Long userId, @NotBlank String name) {}
    public record FleetResource(Long id, Long userId, String name) { static FleetResource from(Fleet f) { return new FleetResource(f.getId(), f.getUserId(), f.getName()); } }
}
