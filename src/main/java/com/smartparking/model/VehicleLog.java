package com.smartparking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_logs")
public class VehicleLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "slot_id", nullable = false)
    private ParkingSlot slot;

    @Column(nullable = false, length = 30)
    private String vehicleNumber;

    @Column(nullable = false)
    private LocalDateTime entryTime = LocalDateTime.now();

    private LocalDateTime exitTime;

    private Double durationHours = 0.0;

    private Double calculatedFee = 0.0;

    private Double overtimeFee = 0.0;

    private Double finalPaidAmount = 0.0;

    @Column(nullable = false, length = 30)
    private String logStatus = "ACTIVE"; // ACTIVE, COMPLETED

    @Column(length = 255)
    private String operatorNotes;

    public VehicleLog() {
    }

    public VehicleLog(Booking booking, ParkingSlot slot, String vehicleNumber, LocalDateTime entryTime) {
        this.booking = booking;
        this.slot = slot;
        this.vehicleNumber = vehicleNumber;
        this.entryTime = entryTime != null ? entryTime : LocalDateTime.now();
        this.logStatus = "ACTIVE";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public ParkingSlot getSlot() {
        return slot;
    }

    public void setSlot(ParkingSlot slot) {
        this.slot = slot;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public Double getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Double durationHours) {
        this.durationHours = durationHours;
    }

    public Double getCalculatedFee() {
        return calculatedFee;
    }

    public void setCalculatedFee(Double calculatedFee) {
        this.calculatedFee = calculatedFee;
    }

    public Double getOvertimeFee() {
        return overtimeFee;
    }

    public void setOvertimeFee(Double overtimeFee) {
        this.overtimeFee = overtimeFee;
    }

    public Double getFinalPaidAmount() {
        return finalPaidAmount;
    }

    public void setFinalPaidAmount(Double finalPaidAmount) {
        this.finalPaidAmount = finalPaidAmount;
    }

    public String getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(String logStatus) {
        this.logStatus = logStatus;
    }

    public String getOperatorNotes() {
        return operatorNotes;
    }

    public void setOperatorNotes(String operatorNotes) {
        this.operatorNotes = operatorNotes;
    }
}
