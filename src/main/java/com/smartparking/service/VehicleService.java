package com.smartparking.service;

import com.smartparking.dto.VehicleEntryRequest;
import com.smartparking.dto.VehicleExitRequest;
import com.smartparking.dto.VehicleGateResponse;
import com.smartparking.model.*;
import com.smartparking.repository.BookingRepository;
import com.smartparking.repository.ParkingSlotRepository;
import com.smartparking.repository.VehicleLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ParkingSlotRepository slotRepository;

    @Autowired
    private VehicleLogRepository vehicleLogRepository;

    @Transactional
    public VehicleGateResponse checkInVehicle(VehicleEntryRequest request) {
        String code = request.getTicketCode().trim();
        Booking booking = bookingRepository.findByQrTicketCode(code)
                .or(() -> bookingRepository.findByBookingCode(code))
                .orElse(null);

        if (booking == null && request.getVehicleNumber() != null && !request.getVehicleNumber().trim().isEmpty()) {
            List<Booking> bookings = bookingRepository.findByVehicleNumberAndStatus(
                    request.getVehicleNumber().trim().toUpperCase(), BookingStatus.CONFIRMED);
            if (!bookings.isEmpty()) {
                booking = bookings.get(0);
            }
        }

        if (booking == null) {
            List<Booking> bookings = bookingRepository.findByVehicleNumberAndStatus(
                    code.toUpperCase(), BookingStatus.CONFIRMED);
            if (!bookings.isEmpty()) {
                booking = bookings.get(0);
            }
        }

        if (booking == null) {
            VehicleGateResponse response = new VehicleGateResponse();
            response.setAllowed(false);
            response.setMessage("Invalid Ticket Code or Vehicle Number. No active booking found!");
            response.setActionType("ENTRY");
            return response;
        }

        if (booking.getStatus() == BookingStatus.ACTIVE) {
            VehicleGateResponse response = new VehicleGateResponse();
            response.setAllowed(false);
            response.setMessage("Vehicle has already entered and is currently parked in Slot " + booking.getSlot().getSlotNumber());
            response.setActionType("ENTRY");
            return response;
        }

        if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELLED) {
            VehicleGateResponse response = new VehicleGateResponse();
            response.setAllowed(false);
            response.setMessage("This booking is " + booking.getStatus() + ". Entry denied.");
            response.setActionType("ENTRY");
            return response;
        }

        LocalDateTime now = LocalDateTime.now();
        booking.setStatus(BookingStatus.ACTIVE);
        booking.setEntryTime(now);
        bookingRepository.save(booking);

        ParkingSlot slot = booking.getSlot();
        slot.setStatus(SlotStatus.OCCUPIED);
        slotRepository.save(slot);

        VehicleLog log = new VehicleLog(booking, slot, booking.getVehicleNumber(), now);
        if (request.getOperatorNotes() != null) {
            log.setOperatorNotes(request.getOperatorNotes());
        }
        vehicleLogRepository.save(log);

        VehicleGateResponse response = new VehicleGateResponse();
        response.setAllowed(true);
        response.setMessage("Gate Opened! Vehicle checked in successfully.");
        response.setVehicleNumber(booking.getVehicleNumber());
        response.setSlotNumber(slot.getSlotNumber());
        if (slot.getLocation() != null) {
            response.setLocationName(slot.getLocation().getName());
        }
        response.setEntryTime(now);
        response.setBaseFee(booking.getTotalFee());
        response.setActionType("ENTRY");

        return response;
    }

    @Transactional
    public VehicleGateResponse checkOutVehicle(VehicleExitRequest request) {
        String code = request.getTicketCode().trim();
        Booking booking = bookingRepository.findByQrTicketCode(code)
                .or(() -> bookingRepository.findByBookingCode(code))
                .orElse(null);

        if (booking == null) {
            List<Booking> bookings = bookingRepository.findByVehicleNumberAndStatus(
                    code.toUpperCase(), BookingStatus.ACTIVE);
            if (!bookings.isEmpty()) {
                booking = bookings.get(0);
            }
        }

        if (booking == null) {
            VehicleGateResponse response = new VehicleGateResponse();
            response.setAllowed(false);
            response.setMessage("No active vehicle found with ticket or vehicle number: " + code);
            response.setActionType("EXIT");
            return response;
        }

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            VehicleGateResponse response = new VehicleGateResponse();
            response.setAllowed(false);
            response.setMessage("Booking status is " + booking.getStatus() + ". Only ACTIVE vehicles can exit.");
            response.setActionType("EXIT");
            return response;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryTime = booking.getEntryTime() != null ? booking.getEntryTime() : booking.getStartTime();
        long elapsedMinutes = Math.max(1, Duration.between(entryTime, now).toMinutes());
        double actualHours = Math.max(1.0, Math.ceil(elapsedMinutes / 60.0));

        double baseFee = booking.getTotalFee();
        double overtimeFee = 0.0;
        if (actualHours > booking.getEstimatedHours()) {
            double extraHours = actualHours - booking.getEstimatedHours();
            overtimeFee = extraHours * booking.getHourlyRate();
        }
        double finalPaid = baseFee + overtimeFee;

        booking.setExitTime(now);
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setTotalFee(finalPaid);
        bookingRepository.save(booking);

        ParkingSlot slot = booking.getSlot();
        slot.setStatus(SlotStatus.AVAILABLE);
        slotRepository.save(slot);

        VehicleLog log = vehicleLogRepository.findByBookingIdAndLogStatus(booking.getId(), "ACTIVE")
                .orElse(new VehicleLog(booking, slot, booking.getVehicleNumber(), entryTime));

        log.setExitTime(now);
        log.setDurationHours(actualHours);
        log.setCalculatedFee(baseFee);
        log.setOvertimeFee(overtimeFee);
        log.setFinalPaidAmount(finalPaid);
        log.setLogStatus("COMPLETED");
        if (request.getOperatorNotes() != null) {
            log.setOperatorNotes(request.getOperatorNotes());
        }
        vehicleLogRepository.save(log);

        VehicleGateResponse response = new VehicleGateResponse();
        response.setAllowed(true);
        response.setMessage("Gate Opened! Vehicle checked out successfully. Have a safe journey!");
        response.setVehicleNumber(booking.getVehicleNumber());
        response.setSlotNumber(slot.getSlotNumber());
        if (slot.getLocation() != null) {
            response.setLocationName(slot.getLocation().getName());
        }
        response.setEntryTime(entryTime);
        response.setExitTime(now);
        response.setDurationHours(actualHours);
        response.setBaseFee(baseFee);
        response.setOvertimeFee(overtimeFee);
        response.setTotalPaid(finalPaid);
        response.setActionType("EXIT");

        return response;
    }

    public List<VehicleLog> getActiveVehicleLogs() {
        return vehicleLogRepository.findByLogStatusOrderByEntryTimeDesc("ACTIVE");
    }

    public List<VehicleLog> getAllVehicleLogs() {
        return vehicleLogRepository.findAllByOrderByEntryTimeDesc();
    }
}
