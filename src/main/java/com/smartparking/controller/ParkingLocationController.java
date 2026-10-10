package com.smartparking.controller;

import com.smartparking.dto.ApiResponse;
import com.smartparking.model.ParkingLocation;
import com.smartparking.service.ParkingLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class ParkingLocationController {

    @Autowired
    private ParkingLocationService locationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ParkingLocation>>> getAllLocations(
            @RequestParam(required = false) String search) {
        List<ParkingLocation> locations = (search != null && !search.isEmpty())
                ? locationService.searchLocations(search)
                : locationService.getAllLocations();
        return ResponseEntity.ok(ApiResponse.success("Locations retrieved", locations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLocationById(@PathVariable Long id) {
        try {
            ParkingLocation location = locationService.getLocationById(id);
            return ResponseEntity.ok(ApiResponse.success("Location retrieved", location));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createLocation(@RequestBody ParkingLocation location) {
        try {
            ParkingLocation created = locationService.createLocation(location);
            return ResponseEntity.ok(ApiResponse.success("Location created successfully", created));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateLocation(@PathVariable Long id, @RequestBody ParkingLocation location) {
        try {
            ParkingLocation updated = locationService.updateLocation(id, location);
            return ResponseEntity.ok(ApiResponse.success("Location updated successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteLocation(@PathVariable Long id) {
        try {
            locationService.deleteLocation(id);
            return ResponseEntity.ok(ApiResponse.success("Location deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
