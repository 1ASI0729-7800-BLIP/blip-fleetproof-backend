package com.blip.fleetproof.platform.vehicleinformation.interfaces.rest;

import com.blip.fleetproof.platform.vehicleinformation.application.internal.VehicleApplicationService;
import com.blip.fleetproof.platform.vehicleinformation.domain.model.aggregates.Vehicle;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {
    private final VehicleApplicationService service;

    public VehicleController(VehicleApplicationService service) { this.service = service; }

    @GetMapping
    public List<VehicleResource> getAll(@RequestParam(required = false) Long userId) {
        return service.findAll(userId).stream().map(VehicleResource::from).toList();
    }

    @GetMapping("/{id}")
    public VehicleResource getById(@PathVariable Long id) { return VehicleResource.from(service.findById(id)); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResource create(@Valid @RequestBody VehicleRequest request) {
        return VehicleResource.from(service.create(request.toDomain()));
    }

    @PutMapping("/{id}")
    public VehicleResource update(@PathVariable Long id, @Valid @RequestBody VehicleRequest request) {
        return VehicleResource.from(service.update(id, request.toDomain()));
    }

    public record VehicleRequest(@NotNull @Positive Long userId, @NotNull @Positive Long fleetId,
                                 @NotBlank String plate, @NotBlank String brand, @NotBlank String model,
                                 @NotNull Integer year, @NotBlank String responsible, String vin, String owner) {
        Vehicle toDomain() { return new Vehicle(userId, fleetId, plate, brand, model, year, responsible, vin, owner); }
    }

    public record VehicleResource(Long id, Long userId, Long fleetId, String plate, String brand, String model,
                                  Integer year, String responsible, String vin, String owner) {
        static VehicleResource from(Vehicle v) {
            return new VehicleResource(v.getId(), v.getUserId(), v.getFleetId(), v.getPlate(), v.getBrand(),
                    v.getModel(), v.getYear(), v.getResponsible(), v.getVin(), v.getOwner());
        }
    }
}
