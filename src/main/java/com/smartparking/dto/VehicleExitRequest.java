package com.smartparking.dto;

import jakarta.validation.constraints.NotBlank;

public class VehicleExitRequest {
    @NotBlank(message = "Ticket code or booking code is required")
    private String ticketCode;

    private String operatorNotes;

    public VehicleExitRequest() {}

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getOperatorNotes() {
        return operatorNotes;
    }

    public void setOperatorNotes(String operatorNotes) {
        this.operatorNotes = operatorNotes;
    }
}
