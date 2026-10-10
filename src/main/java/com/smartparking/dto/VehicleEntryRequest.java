package com.smartparking.dto;

import jakarta.validation.constraints.NotBlank;

public class VehicleEntryRequest {
    @NotBlank(message = "Ticket code or booking code is required")
    private String ticketCode;

    private String vehicleNumber;
    private String operatorNotes;

    public VehicleEntryRequest() {}

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getOperatorNotes() {
        return operatorNotes;
    }

    public void setOperatorNotes(String operatorNotes) {
        this.operatorNotes = operatorNotes;
    }
}
