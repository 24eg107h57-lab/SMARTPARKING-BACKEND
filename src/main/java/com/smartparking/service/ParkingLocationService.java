package com.smartparking.service;

import com.smartparking.model.ParkingLocation;
import com.smartparking.repository.ParkingLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingLocationService {

    @Autowired
    private ParkingLocationRepository locationRepository;

    public List<ParkingLocation> getAllLocations() {
        return locationRepository.findAll();
    }

    public ParkingLocation getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
    }

    public ParkingLocation createLocation(ParkingLocation location) {
        return locationRepository.save(location);
    }

    public ParkingLocation updateLocation(Long id, ParkingLocation updated) {
        ParkingLocation existing = getLocationById(id);
        existing.setName(updated.getName());
        existing.setAddress(updated.getAddress());
        existing.setCity(updated.getCity());
        existing.setDescription(updated.getDescription());
        existing.setCarHourlyRate(updated.getCarHourlyRate());
        existing.setBikeHourlyRate(updated.getBikeHourlyRate());
        existing.setEvHourlyRate(updated.getEvHourlyRate());
        existing.setTruckHourlyRate(updated.getTruckHourlyRate());
        existing.setTotalSlots(updated.getTotalSlots());
        existing.setImageUrl(updated.getImageUrl());
        return locationRepository.save(existing);
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    public List<ParkingLocation> searchLocations(String query) {
        if (query == null || query.trim().isEmpty()) {
            return locationRepository.findAll();
        }
        return locationRepository.findByNameContainingIgnoreCase(query.trim());
    }
}
