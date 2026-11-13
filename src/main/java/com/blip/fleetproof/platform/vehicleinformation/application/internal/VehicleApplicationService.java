package com.blip.fleetproof.platform.vehicleinformation.application.internal;

import com.blip.fleetproof.platform.vehicleinformation.domain.model.aggregates.Vehicle;
import com.blip.fleetproof.platform.vehicleinformation.infrastructure.persistence.jpa.entities.VehicleEntity;
import com.blip.fleetproof.platform.vehicleinformation.infrastructure.persistence.jpa.repositories.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VehicleApplicationService {
    private final VehicleRepository repository;

    public VehicleApplicationService(VehicleRepository repository) { this.repository = repository; }

    @Transactional(readOnly = true)
    public List<Vehicle> findAll(Long userId) {
        var entities = userId == null ? repository.findAll() : repository.findAllByUserId(userId);
        return entities.stream().map(this::toDomain).toList();
    }

    @Transactional(readOnly = true)
    public Vehicle findById(Long id) {
        return repository.findById(id).map(this::toDomain)
                .orElseThrow(() -> new NoSuchElementException("Vehicle not found"));
    }

    @Transactional
    public Vehicle create(Vehicle vehicle) {
        if (repository.existsByPlateIgnoreCase(vehicle.getPlate()))
            throw new IllegalArgumentException("plate already exists");
        return toDomain(repository.save(toEntity(vehicle, new VehicleEntity())));
    }

    @Transactional
    public Vehicle update(Long id, Vehicle changes) {
        var entity = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Vehicle not found"));
        return toDomain(repository.save(toEntity(changes, entity)));
    }

    private VehicleEntity toEntity(Vehicle source, VehicleEntity target) {
        target.setUserId(source.getUserId()); target.setFleetId(source.getFleetId());
        target.setPlate(source.getPlate()); target.setBrand(source.getBrand()); target.setModel(source.getModel());
        target.setYear(source.getYear()); target.setResponsible(source.getResponsible());
        target.setVin(source.getVin()); target.setOwner(source.getOwner());
        return target;
    }

    private Vehicle toDomain(VehicleEntity entity) {
        return Vehicle.reconstitute(entity.getId(), entity.getUserId(), entity.getFleetId(), entity.getPlate(),
                entity.getBrand(), entity.getModel(), entity.getYear(), entity.getResponsible(),
                entity.getVin(), entity.getOwner());
    }
}
