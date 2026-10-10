package com.smartparking.dto;

import com.smartparking.model.Booking;
import com.smartparking.model.BookingStatus;
import com.smartparking.model.PaymentStatus;
import com.smartparking.model.VehicleType;
import java.time.LocalDateTime;

public class BookingResponseDTO {
    private Long id;
    private String bookingCode;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long slotId;
    private String slotNumber;
    private Integer floor;
    private String zone;
    private Long locationId;
    private String locationName;
    private String locationAddress;
    private String vehicleNumber;
    private VehicleType vehicleType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
    private Double hourlyRate;
    private Double estimatedHours;
    private Double totalFee;
    private PaymentStatus paymentStatus;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private String qrTicketCode;
    private LocalDateTime createdAt;

    public BookingResponseDTO() {}

    public BookingResponseDTO(Booking booking) {
        this.id = booking.getId();
        this.bookingCode = booking.getBookingCode();
        if (booking.getUser() != null) {
            this.userId = booking.getUser().getId();
            this.userName = booking.getUser().getFullName();
            this.userEmail = booking.getUser().getEmail();
        }
        if (booking.getSlot() != null) {
            this.slotId = booking.getSlot().getId();
            this.slotNumber = booking.getSlot().getSlotNumber();
            this.floor = booking.getSlot().getFloor();
            this.zone = booking.getSlot().getZone();
            if (booking.getSlot().getLocation() != null) {
                this.locationId = booking.getSlot().getLocation().getId();
                this.locationName = booking.getSlot().getLocation().getName();
                this.locationAddress = booking.getSlot().getLocation().getAddress();
            }
        }
        this.vehicleNumber = booking.getVehicleNumber();
        this.vehicleType = booking.getVehicleType();
        this.startTime = booking.getStartTime();
        this.endTime = booking.getEndTime();
        this.status = booking.getStatus();
        this.hourlyRate = booking.getHourlyRate();
        this.estimatedHours = booking.getEstimatedHours();
        this.totalFee = booking.getTotalFee();
        this.paymentStatus = booking.getPaymentStatus();
        this.entryTime = booking.getEntryTime();
        this.exitTime = booking.getExitTime();
        this.qrTicketCode = booking.getQrTicketCode();
        this.createdAt = booking.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
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

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Double getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(Double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public String getQrTicketCode() {
        return qrTicketCode;
    }

    public void setQrTicketCode(String qrTicketCode) {
        this.qrTicketCode = qrTicketCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
