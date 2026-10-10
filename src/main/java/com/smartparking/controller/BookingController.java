package com.smartparking.controller;

import com.smartparking.dto.ApiResponse;
import com.smartparking.dto.BookingRequest;
import com.smartparking.dto.BookingResponseDTO;
import com.smartparking.dto.FeeCalculationRequest;
import com.smartparking.dto.FeeCalculationResponse;
import com.smartparking.model.User;
import com.smartparking.repository.UserRepository;
import com.smartparking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest request) {
        try {
            User user = getAuthenticatedUser();
            BookingResponseDTO booking = bookingService.createBooking(user, request);
            return ResponseEntity.ok(ApiResponse.success("Booking confirmed successfully!", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getMyBookings() {
        User user = getAuthenticatedUser();
        List<BookingResponseDTO> bookings = bookingService.getUserBookings(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Bookings retrieved", bookings));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        try {
            BookingResponseDTO booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(ApiResponse.success("Booking retrieved", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        try {
            User user = getAuthenticatedUser();
            BookingResponseDTO cancelled = bookingService.cancelBooking(id, user);
            return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully and slot freed", cancelled));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/calculate-fee")
    public ResponseEntity<ApiResponse<FeeCalculationResponse>> calculateFee(
            @RequestBody FeeCalculationRequest request) {
        FeeCalculationResponse response = bookingService.calculateFee(request);
        return ResponseEntity.ok(ApiResponse.success("Fee calculated successfully", response));
    }

    @GetMapping("/ticket/{ticketCode}")
    public ResponseEntity<?> getBookingByTicket(@PathVariable String ticketCode) {
        try {
            BookingResponseDTO booking = bookingService.getBookingByTicketCode(ticketCode);
            return ResponseEntity.ok(ApiResponse.success("Booking found", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
