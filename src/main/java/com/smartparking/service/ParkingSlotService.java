package com.smartparking.service;

import com.smartparking.dto.SlotCreateRequest;
import com.smartparking.dto.SlotDTO;
import com.smartparking.model.ParkingLocation;
import com.smartparking.model.ParkingSlot;
import com.smartparking.model.SlotStatus;
import com.smartparking.model.VehicleType;
import com.smartparking.repository.ParkingLocationRepository;
import com.smartparking.repository.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingSlotService {

    @Autowired
    private ParkingSlotRepository slotRepository;

    @Autowired
    private ParkingLocationRepository locationRepository;

    public List<SlotDTO> getAllSlots() {
        return slotRepository.findAll().stream()
                .map(SlotDTO::new)
                .collect(Collectors.toList());
    }

    public SlotDTO getSlotById(Long id) {
        ParkingSlot slot = slotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot not found with id: " + id));
        return new SlotDTO(slot);
    }

    public List<SlotDTO> getSlotsByLocation(Long locationId) {
        return slotRepository.findByLocationId(locationId).stream()
                .map(SlotDTO::new)
                .collect(Collectors.toList());
    }

    public List<SlotDTO> getAvailableSlots() {
        return slotRepository.findByStatus(SlotStatus.AVAILABLE).stream()
                .map(SlotDTO::new)
                .collect(Collectors.toList());
    }

    public List<SlotDTO> filterSlots(Long locationId, VehicleType vehicleType, SlotStatus status) {
        List<ParkingSlot> slots;
        if (locationId != null && vehicleType != null && status != null) {
            slots = slotRepository.findByLocationIdAndVehicleTypeAndStatus(locationId, vehicleType, status);
        } else if (locationId != null && vehicleType != null) {
            slots = slotRepository.findByLocationIdAndVehicleType(locationId, vehicleType);
        } else if (locationId != null && status != null) {
            slots = slotRepository.findByLocationIdAndStatus(locationId, status);
        } else if (locationId != null) {
            slots = slotRepository.findByLocationId(locationId);
        } else if (status != null) {
            slots = slotRepository.findByStatus(status);
        } else {
            slots = slotRepository.findAll();
        }

        if (vehicleType != null && locationId == null) {
            slots = slots.stream().filter(s -> s.getVehicleType() == vehicleType).collect(Collectors.toList());
        }

        return slots.stream().map(SlotDTO::new).collect(Collectors.toList());
    }

    public SlotDTO createSlot(SlotCreateRequest request) {
        ParkingLocation location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + request.getLocationId()));

        if (slotRepository.findByLocationIdAndSlotNumber(location.getId(), request.getSlotNumber()).isPresent()) {
            throw new RuntimeException("Slot number " + request.getSlotNumber() + " already exists in this location!");
        }

        ParkingSlot slot = new ParkingSlot(
                request.getSlotNumber(),
                request.getFloor(),
                request.getZone(),
                request.getVehicleType(),
                request.getStatus() != null ? request.getStatus() : SlotStatus.AVAILABLE,
                location,
                request.getCustomHourlyRate(),
                request.getIsEvCharging()
        );

        ParkingSlot saved = slotRepository.save(slot);
        location.setTotalSlots(location.getTotalSlots() + 1);
        locationRepository.save(location);

        return new SlotDTO(saved);
    }

    public SlotDTO updateSlot(Long id, SlotCreateRequest request) {
        ParkingSlot slot = slotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot not found with id: " + id));

        slot.setSlotNumber(request.getSlotNumber());
        slot.setFloor(request.getFloor());
        slot.setZone(request.getZone());
        slot.setVehicleType(request.getVehicleType());
        if (request.getStatus() != null) {
            slot.setStatus(request.getStatus());
        }
        slot.setCustomHourlyRate(request.getCustomHourlyRate());
        slot.setIsEvCharging(request.getIsEvCharging());

        if (request.getLocationId() != null && !request.getLocationId().equals(slot.getLocation().getId())) {
            ParkingLocation newLoc = locationRepository.findById(request.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found"));
            slot.setLocation(newLoc);
        }

        return new SlotDTO(slotRepository.save(slot));
    }

    public SlotDTO updateSlotStatus(Long id, SlotStatus status) {
        ParkingSlot slot = slotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot not found with id: " + id));
        slot.setStatus(status);
        return new SlotDTO(slotRepository.save(slot));
    }

    public void deleteSlot(Long id) {
        ParkingSlot slot = slotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot not found with id: " + id));
        ParkingLocation location = slot.getLocation();
        slotRepository.deleteById(id);
        if (location != null && location.getTotalSlots() > 0) {
            location.setTotalSlots(location.getTotalSlots() - 1);
            locationRepository.save(location);
        }
    }
}
