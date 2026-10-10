package com.smartparking.controller;

import com.smartparking.dto.ApiResponse;
import com.smartparking.dto.BookingResponseDTO;
import com.smartparking.dto.VehicleEntryRequest;
import com.smartparking.dto.VehicleExitRequest;
import com.smartparking.dto.VehicleGateResponse;
import com.smartparking.model.VehicleLog;
import com.smartparking.service.BookingService;
import com.smartparking.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private BookingService bookingService;

    @PostMapping("/entry")
    public ResponseEntity<ApiResponse<VehicleGateResponse>> checkInVehicle(
            @Valid @RequestBody VehicleEntryRequest request) {
        VehicleGateResponse response = vehicleService.checkInVehicle(request);
        if (response.isAllowed()) {
            return ResponseEntity.ok(ApiResponse.success(response.getMessage(), response));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error(response.getMessage()));
        }
    }

    @PostMapping("/exit")
    public ResponseEntity<ApiResponse<VehicleGateResponse>> checkOutVehicle(
            @Valid @RequestBody VehicleExitRequest request) {
        VehicleGateResponse response = vehicleService.checkOutVehicle(request);
        if (response.isAllowed()) {
            return ResponseEntity.ok(ApiResponse.success(response.getMessage(), response));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error(response.getMessage()));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<VehicleLog>>> getActiveVehicles() {
        return ResponseEntity.ok(ApiResponse.success("Active vehicles retrieved", vehicleService.getActiveVehicleLogs()));
    }

    @GetMapping("/logs")
    public ResponseEntity<ApiResponse<List<VehicleLog>>> getAllLogs() {
        return ResponseEntity.ok(ApiResponse.success("Vehicle logs retrieved", vehicleService.getAllVehicleLogs()));
    }

    @GetMapping("/verify-ticket/{ticketCode}")
    public ResponseEntity<?> verifyTicket(@PathVariable String ticketCode) {
        try {
            BookingResponseDTO booking = bookingService.getBookingByTicketCode(ticketCode);
            return ResponseEntity.ok(ApiResponse.success("Ticket verified", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Ticket not found or invalid: " + ticketCode));
        }
    }
}
