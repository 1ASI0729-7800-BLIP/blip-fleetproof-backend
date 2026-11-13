package com.blip.fleetproof.platform.fleetmanagement.application.internal;

import com.blip.fleetproof.platform.fleetmanagement.domain.model.aggregates.Fleet;
import com.blip.fleetproof.platform.fleetmanagement.infrastructure.persistence.jpa.entities.FleetEntity;
import com.blip.fleetproof.platform.fleetmanagement.infrastructure.persistence.jpa.repositories.FleetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class FleetApplicationService {
    private final FleetRepository repository;
    public FleetApplicationService(FleetRepository repository) { this.repository = repository; }
    @Transactional(readOnly = true) public List<Fleet> findAll(Long userId) {
        return (userId == null ? repository.findAll() : repository.findAllByUserId(userId)).stream().map(this::domain).toList();
    }
    @Transactional public Fleet create(Fleet fleet) { var entity = new FleetEntity(); entity.setUserId(fleet.getUserId()); entity.setName(fleet.getName()); return domain(repository.save(entity)); }
    private Fleet domain(FleetEntity e) { return Fleet.reconstitute(e.getId(), e.getUserId(), e.getName()); }
}
