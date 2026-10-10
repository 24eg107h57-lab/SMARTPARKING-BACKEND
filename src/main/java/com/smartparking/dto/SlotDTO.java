package com.smartparking.dto;

import com.smartparking.model.ParkingSlot;
import com.smartparking.model.SlotStatus;
import com.smartparking.model.VehicleType;

public class SlotDTO {
    private Long id;
    private String slotNumber;
    private Integer floor;
    private String zone;
    private VehicleType vehicleType;
    private SlotStatus status;
    private Long locationId;
    private String locationName;
    private String locationAddress;
    private String locationCity;
    private Double hourlyRate;
    private Boolean isEvCharging;

    public SlotDTO() {}

    public SlotDTO(ParkingSlot slot) {
        this.id = slot.getId();
        this.slotNumber = slot.getSlotNumber();
        this.floor = slot.getFloor();
        this.zone = slot.getZone();
        this.vehicleType = slot.getVehicleType();
        this.status = slot.getStatus();
        if (slot.getLocation() != null) {
            this.locationId = slot.getLocation().getId();
            this.locationName = slot.getLocation().getName();
            this.locationAddress = slot.getLocation().getAddress();
            this.locationCity = slot.getLocation().getCity();
        }
        this.hourlyRate = slot.getEffectiveHourlyRate();
        this.isEvCharging = slot.getIsEvCharging();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Boolean getIsEvCharging() {
        return isEvCharging;
    }

    public void setIsEvCharging(Boolean isEvCharging) {
        this.isEvCharging = isEvCharging;
    }
}
