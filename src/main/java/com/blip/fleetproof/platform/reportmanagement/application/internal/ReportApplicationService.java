package com.blip.fleetproof.platform.reportmanagement.application.internal;

import com.blip.fleetproof.platform.reportmanagement.domain.model.aggregates.VehicleReport;
import com.blip.fleetproof.platform.reportmanagement.domain.model.valueobjects.Finding;
import com.blip.fleetproof.platform.reportmanagement.domain.model.valueobjects.SourceCheck;
import com.blip.fleetproof.platform.reportmanagement.infrastructure.persistence.jpa.entities.*;
import com.blip.fleetproof.platform.reportmanagement.infrastructure.persistence.jpa.repositories.VehicleReportRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReportApplicationService {
    private final VehicleReportRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public ReportApplicationService(VehicleReportRepository repository) {
        this.repository = repository;
    }
    @Transactional(readOnly = true) public List<VehicleReport> findAll(Long userId, Long vehicleId) {
        var entities = vehicleId != null ? repository.findAllByVehicleIdOrderByVersionAsc(vehicleId)
                : userId != null ? repository.findAllByUserId(userId) : repository.findAll();
        return entities.stream().map(this::domain).toList();
    }
    @Transactional(readOnly = true) public VehicleReport findById(Long id) {
        return repository.findById(id).map(this::domain).orElseThrow(() -> new NoSuchElementException("Report not found"));
    }
    @Transactional public VehicleReport create(VehicleReport report) { return domain(repository.save(entity(report))); }
    private VehicleReportEntity entity(VehicleReport r) {
        var e = new VehicleReportEntity(); e.setUserId(r.getUserId()); e.setVehicleId(r.getVehicleId());
        e.setPlate(r.getPlate()); e.setOccurredAt(r.getCreatedAt()); e.setVersion(r.getVersion());
        e.setSourcesJson(writeSources(r.getSources())); return e;
    }
    private VehicleReport domain(VehicleReportEntity e) {
        return VehicleReport.reconstitute(e.getId(), e.getUserId(), e.getVehicleId(), e.getPlate(),
                e.getOccurredAt(), e.getVersion(), readSources(e.getSourcesJson()));
    }
    private String writeSources(List<SourceCheck> sources) {
        try { return objectMapper.writeValueAsString(sources); }
        catch (JsonProcessingException exception) { throw new IllegalArgumentException("sources cannot be serialized", exception); }
    }
    private List<SourceCheck> readSources(String json) {
        try { return objectMapper.readValue(json, new TypeReference<>() {}); }
        catch (JsonProcessingException exception) { throw new IllegalStateException("stored sources are invalid", exception); }
    }
}
