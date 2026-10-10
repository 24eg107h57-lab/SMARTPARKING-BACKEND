package com.smartparking.repository;

import com.smartparking.model.ParkingSlot;
import com.smartparking.model.SlotStatus;
import com.smartparking.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    List<ParkingSlot> findByLocationId(Long locationId);
    List<ParkingSlot> findByStatus(SlotStatus status);
    List<ParkingSlot> findByLocationIdAndStatus(Long locationId, SlotStatus status);
    List<ParkingSlot> findByLocationIdAndVehicleType(Long locationId, VehicleType vehicleType);
    List<ParkingSlot> findByLocationIdAndVehicleTypeAndStatus(Long locationId, VehicleType vehicleType, SlotStatus status);
    Optional<ParkingSlot> findByLocationIdAndSlotNumber(Long locationId, String slotNumber);
    long countByStatus(SlotStatus status);
    long countByLocationIdAndStatus(Long locationId, SlotStatus status);
    long countByLocationId(Long locationId);
}
