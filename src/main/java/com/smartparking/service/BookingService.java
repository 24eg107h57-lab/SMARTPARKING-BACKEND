package com.smartparking.service;

import com.smartparking.dto.BookingRequest;
import com.smartparking.dto.BookingResponseDTO;
import com.smartparking.dto.FeeCalculationRequest;
import com.smartparking.dto.FeeCalculationResponse;
import com.smartparking.model.*;
import com.smartparking.repository.BookingRepository;
import com.smartparking.repository.ParkingLocationRepository;
import com.smartparking.repository.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ParkingSlotRepository slotRepository;

    @Autowired
    private ParkingLocationRepository locationRepository;

    @Transactional
    public BookingResponseDTO createBooking(User user, BookingRequest request) {
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new RuntimeException("End time must be after start time");
        }

        ParkingSlot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Parking slot not found"));

        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new RuntimeException("Selected slot is currently not available (Status: " + slot.getStatus() + ")");
        }

        // Calculate hours and fee
        long minutes = Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();
        if (minutes < 30) {
            minutes = 60; // minimum 1 hour billing
        }
        double estimatedHours = Math.max(1.0, Math.ceil(minutes / 60.0));
        double hourlyRate = slot.getEffectiveHourlyRate();
        double totalFee = Math.round(estimatedHours * hourlyRate * 100.0) / 100.0;

        String bookingCode = "PRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Booking booking = new Booking(
                bookingCode,
                user,
                slot,
                request.getVehicleNumber().toUpperCase().trim(),
                request.getVehicleType() != null ? request.getVehicleType() : slot.getVehicleType(),
                request.getStartTime(),
                request.getEndTime(),
                hourlyRate,
                estimatedHours,
                totalFee,
                PaymentStatus.PAID
        );

        // Update slot status to RESERVED
        slot.setStatus(SlotStatus.RESERVED);
        slotRepository.save(slot);

        Booking saved = bookingRepository.save(booking);
        return new BookingResponseDTO(saved);
    }

    @Transactional
    public BookingResponseDTO cancelBooking(Long bookingId, User currentUser) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;
        if (!isAdmin && !booking.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to cancel this booking");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel a completed booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setPaymentStatus(PaymentStatus.REFUNDED);

        // Free the slot back to AVAILABLE
        ParkingSlot slot = booking.getSlot();
        if (slot != null && (slot.getStatus() == SlotStatus.RESERVED || slot.getStatus() == SlotStatus.OCCUPIED)) {
            slot.setStatus(SlotStatus.AVAILABLE);
            slotRepository.save(slot);
        }

        return new BookingResponseDTO(bookingRepository.save(booking));
    }

    public List<BookingResponseDTO> getUserBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(BookingResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(BookingResponseDTO::new)
                .collect(Collectors.toList());
    }

    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        return new BookingResponseDTO(booking);
    }

    public BookingResponseDTO getBookingByCode(String bookingCode) {
        Booking booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new RuntimeException("Booking not found with code: " + bookingCode));
        return new BookingResponseDTO(booking);
    }

    public BookingResponseDTO getBookingByTicketCode(String ticketCode) {
        Booking booking = bookingRepository.findByQrTicketCode(ticketCode)
                .or(() -> bookingRepository.findByBookingCode(ticketCode))
                .orElseThrow(() -> new RuntimeException("Booking not found with ticket code: " + ticketCode));
        return new BookingResponseDTO(booking);
    }

    public FeeCalculationResponse calculateFee(FeeCalculationRequest request) {
        double hourlyRate = 50.0;

        if (request.getSlotId() != null) {
            ParkingSlot slot = slotRepository.findById(request.getSlotId()).orElse(null);
            if (slot != null) {
                hourlyRate = slot.getEffectiveHourlyRate();
            }
        } else if (request.getLocationId() != null) {
            ParkingLocation loc = locationRepository.findById(request.getLocationId()).orElse(null);
            if (loc != null && request.getVehicleType() != null) {
                hourlyRate = switch (request.getVehicleType()) {
                    case BIKE -> loc.getBikeHourlyRate();
                    case EV -> loc.getEvHourlyRate();
                    case TRUCK -> loc.getTruckHourlyRate();
                    default -> loc.getCarHourlyRate();
                };
            }
        }

        long minutes = 60;
        if (request.getStartTime() != null && request.getEndTime() != null) {
            minutes = Math.max(30, Duration.between(request.getStartTime(), request.getEndTime()).toMinutes());
        }
        double hours = Math.max(1.0, Math.ceil(minutes / 60.0));
        double baseFee = Math.round(hours * hourlyRate * 100.0) / 100.0;
        double taxAmount = Math.round(baseFee * 0.05 * 100.0) / 100.0; // 5% GST/Tax
        double totalFee = baseFee + taxAmount;

        return new FeeCalculationResponse(hourlyRate, hours, baseFee, taxAmount, totalFee);
    }
}
