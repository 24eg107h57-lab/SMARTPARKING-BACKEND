package com.smartparking.dto;

import com.smartparking.model.SlotStatus;
import com.smartparking.model.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SlotCreateRequest {
    @NotBlank(message = "Slot number is required")
    private String slotNumber;

    @NotNull(message = "Floor is required")
    private Integer floor = 1;

    @NotBlank(message = "Zone is required")
    private String zone = "A";

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType = VehicleType.CAR;

    private SlotStatus status = SlotStatus.AVAILABLE;

    @NotNull(message = "Location ID is required")
    private Long locationId;

    private Double customHourlyRate;

    private Boolean isEvCharging = false;

    public SlotCreateRequest() {}

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

    public Double getCustomHourlyRate() {
        return customHourlyRate;
    }

    public void setCustomHourlyRate(Double customHourlyRate) {
        this.customHourlyRate = customHourlyRate;
    }

    public Boolean getIsEvCharging() {
        return isEvCharging;
    }

    public void setIsEvCharging(Boolean isEvCharging) {
        this.isEvCharging = isEvCharging;
    }
}
