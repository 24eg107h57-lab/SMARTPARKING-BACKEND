package com.smartparking.service;

import com.smartparking.dto.BookingResponseDTO;
import com.smartparking.dto.DashboardStatsDTO;
import com.smartparking.model.BookingStatus;
import com.smartparking.model.SlotStatus;
import com.smartparking.repository.BookingRepository;
import com.smartparking.repository.ParkingSlotRepository;
import com.smartparking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private ParkingSlotRepository slotRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    public DashboardStatsDTO getDashboardStats() {
        long totalSlots = slotRepository.count();
        long availableSlots = slotRepository.countByStatus(SlotStatus.AVAILABLE);
        long occupiedSlots = slotRepository.countByStatus(SlotStatus.OCCUPIED);
        long reservedSlots = slotRepository.countByStatus(SlotStatus.RESERVED);
        long maintenanceSlots = slotRepository.countByStatus(SlotStatus.MAINTENANCE);

        long totalUsers = userRepository.count();
        long totalBookings = bookingRepository.count();
        long activeBookings = bookingRepository.countByStatus(BookingStatus.ACTIVE)
                + bookingRepository.countByStatus(BookingStatus.CONFIRMED);

        Double totalRevenue = bookingRepository.calculateTotalRevenue();
        if (totalRevenue == null) {
            totalRevenue = 0.0;
        }

        double occupancyRate = 0.0;
        if (totalSlots > 0) {
            occupancyRate = Math.round(((double) (occupiedSlots + reservedSlots) / totalSlots) * 1000.0) / 10.0;
        }

        List<BookingResponseDTO> recent = bookingRepository.findRecentBookings(PageRequest.of(0, 10)).stream()
                .map(BookingResponseDTO::new)
                .collect(Collectors.toList());

        DashboardStatsDTO dto = new DashboardStatsDTO();
        dto.setTotalSlots(totalSlots);
        dto.setAvailableSlots(availableSlots);
        dto.setOccupiedSlots(occupiedSlots);
        dto.setReservedSlots(reservedSlots);
        dto.setMaintenanceSlots(maintenanceSlots);
        dto.setTotalUsers(totalUsers);
        dto.setTotalBookings(totalBookings);
        dto.setActiveBookings(activeBookings);
        dto.setTotalRevenue(Math.round(totalRevenue * 100.0) / 100.0);
        dto.setOccupancyRate(occupancyRate);
        dto.setRecentBookings(recent);

        return dto;
    }
}
