package com.smartparking.repository;

import com.smartparking.model.VehicleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleLogRepository extends JpaRepository<VehicleLog, Long> {
    List<VehicleLog> findByLogStatusOrderByEntryTimeDesc(String logStatus);
    Optional<VehicleLog> findByBookingIdAndLogStatus(Long bookingId, String logStatus);
    Optional<VehicleLog> findByVehicleNumberAndLogStatus(String vehicleNumber, String logStatus);
    List<VehicleLog> findAllByOrderByEntryTimeDesc();
}
