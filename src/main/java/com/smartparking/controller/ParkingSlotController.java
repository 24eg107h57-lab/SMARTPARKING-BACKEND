package com.smartparking.controller;

import com.smartparking.dto.ApiResponse;
import com.smartparking.dto.SlotCreateRequest;
import com.smartparking.dto.SlotDTO;
import com.smartparking.model.SlotStatus;
import com.smartparking.model.VehicleType;
import com.smartparking.service.ParkingSlotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
public class ParkingSlotController {

    @Autowired
    private ParkingSlotService slotService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SlotDTO>>> getAllSlots() {
        return ResponseEntity.ok(ApiResponse.success("Slots retrieved", slotService.getAllSlots()));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<SlotDTO>>> getAvailableSlots() {
        return ResponseEntity.ok(ApiResponse.success("Available slots retrieved", slotService.getAvailableSlots()));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<SlotDTO>>> filterSlots(
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) VehicleType vehicleType,
            @RequestParam(required = false) SlotStatus status) {
        List<SlotDTO> slots = slotService.filterSlots(locationId, vehicleType, status);
        return ResponseEntity.ok(ApiResponse.success("Filtered slots retrieved", slots));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<ApiResponse<List<SlotDTO>>> getSlotsByLocation(@PathVariable Long locationId) {
        return ResponseEntity.ok(ApiResponse.success("Location slots retrieved", slotService.getSlotsByLocation(locationId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSlotById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.success("Slot retrieved", slotService.getSlotById(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createSlot(@Valid @RequestBody SlotCreateRequest request) {
        try {
            SlotDTO created = slotService.createSlot(request);
            return ResponseEntity.ok(ApiResponse.success("Slot created successfully", created));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateSlot(@PathVariable Long id, @Valid @RequestBody SlotCreateRequest request) {
        try {
            SlotDTO updated = slotService.updateSlot(id, request);
            return ResponseEntity.ok(ApiResponse.success("Slot updated successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateSlotStatus(@PathVariable Long id, @RequestParam SlotStatus status) {
        try {
            SlotDTO updated = slotService.updateSlotStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success("Slot status updated to " + status, updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteSlot(@PathVariable Long id) {
        try {
            slotService.deleteSlot(id);
            return ResponseEntity.ok(ApiResponse.success("Slot deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
