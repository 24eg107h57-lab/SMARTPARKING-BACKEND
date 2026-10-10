package com.smartparking.repository;

import com.smartparking.model.ParkingLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingLocationRepository extends JpaRepository<ParkingLocation, Long> {
    List<ParkingLocation> findByCityIgnoreCase(String city);
    List<ParkingLocation> findByNameContainingIgnoreCase(String name);
}
