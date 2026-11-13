package com.blip.fleetproof.platform.vehicleinformation.domain.model.aggregates;

import com.blip.fleetproof.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

public class Vehicle extends AbstractDomainAggregateRoot<Vehicle> {
    private Long id;
    private Long userId;
    private Long fleetId;
    private String plate;
    private String brand;
    private String model;
    private Integer year;
    private String responsible;
    private String vin;
    private String owner;

    public Vehicle(Long userId, Long fleetId, String plate, String brand, String model,
                   Integer year, String responsible, String vin, String owner) {
        this.userId = requirePositive(userId, "userId");
        this.fleetId = requirePositive(fleetId, "fleetId");
        this.plate = normalizePlate(plate);
        this.brand = requireText(brand, "brand");
        this.model = requireText(model, "model");
        if (year == null || year < 1900 || year > 2100) throw new IllegalArgumentException("year is invalid");
        this.year = year;
        this.responsible = requireText(responsible, "responsible");
        this.vin = vin == null ? "" : vin.trim().toUpperCase();
        this.owner = owner == null ? "" : owner.trim();
    }

    public static Vehicle reconstitute(Long id, Long userId, Long fleetId, String plate, String brand,
                                       String model, Integer year, String responsible, String vin, String owner) {
        var vehicle = new Vehicle(userId, fleetId, plate, brand, model, year, responsible, vin, owner);
        vehicle.id = id;
        return vehicle;
    }

    public void update(Long userId, Long fleetId, String plate, String brand, String model,
                       Integer year, String responsible, String vin, String owner) {
        this.userId = requirePositive(userId, "userId");
        this.fleetId = requirePositive(fleetId, "fleetId");
        this.plate = normalizePlate(plate);
        this.brand = requireText(brand, "brand");
        this.model = requireText(model, "model");
        if (year == null || year < 1900 || year > 2100) throw new IllegalArgumentException("year is invalid");
        this.year = year;
        this.responsible = requireText(responsible, "responsible");
        this.vin = vin == null ? "" : vin.trim().toUpperCase();
        this.owner = owner == null ? "" : owner.trim();
    }

    private static String normalizePlate(String value) {
        var raw = requireText(value, "plate").toUpperCase().replace("-", "");
        if (!raw.matches("[A-Z0-9]{6}")) throw new IllegalArgumentException("plate is invalid");
        return raw.substring(0, 3) + "-" + raw.substring(3);
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required");
        return value.trim();
    }

    private static Long requirePositive(Long value, String field) {
        if (value == null || value <= 0) throw new IllegalArgumentException(field + " must be positive");
        return value;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getFleetId() { return fleetId; }
    public String getPlate() { return plate; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public Integer getYear() { return year; }
    public String getResponsible() { return responsible; }
    public String getVin() { return vin; }
    public String getOwner() { return owner; }
}
