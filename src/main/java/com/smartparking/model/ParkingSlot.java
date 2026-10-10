package com.smartparking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "parking_slots", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"location_id", "slotNumber"})
})
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String slotNumber;

    @Column(nullable = false)
    private Integer floor = 1;

    @Column(nullable = false, length = 50)
    private String zone = "A";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VehicleType vehicleType = VehicleType.CAR;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SlotStatus status = SlotStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", nullable = false)
    private ParkingLocation location;

    private Double customHourlyRate;

    @Column(nullable = false)
    private Boolean isEvCharging = false;

    public ParkingSlot() {
    }

    public ParkingSlot(String slotNumber, Integer floor, String zone, VehicleType vehicleType,
                       SlotStatus status, ParkingLocation location, Double customHourlyRate, Boolean isEvCharging) {
        this.slotNumber = slotNumber;
        this.floor = floor;
        this.zone = zone;
        this.vehicleType = vehicleType;
        this.status = status;
        this.location = location;
        this.customHourlyRate = customHourlyRate;
        this.isEvCharging = isEvCharging != null ? isEvCharging : false;
    }

    public Double getEffectiveHourlyRate() {
        if (customHourlyRate != null && customHourlyRate > 0) {
            return customHourlyRate;
        }
        if (location != null) {
            return switch (vehicleType) {
                case BIKE -> location.getBikeHourlyRate();
                case EV -> location.getEvHourlyRate();
                case TRUCK -> location.getTruckHourlyRate();
                default -> location.getCarHourlyRate();
            };
        }
        return 50.0;
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

    public ParkingLocation getLocation() {
        return location;
    }

    public void setLocation(ParkingLocation location) {
        this.location = location;
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
