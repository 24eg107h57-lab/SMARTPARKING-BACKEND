package com.smartparking.repository;

import com.smartparking.model.Booking;
import com.smartparking.model.BookingStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Booking> findByBookingCode(String bookingCode);
    Optional<Booking> findByQrTicketCode(String qrTicketCode);
    List<Booking> findBySlotIdAndStatus(Long slotId, BookingStatus status);
    List<Booking> findByVehicleNumberAndStatus(String vehicleNumber, BookingStatus status);
    long countByStatus(BookingStatus status);

    @Query("SELECT COALESCE(SUM(b.totalFee), 0.0) FROM Booking b WHERE b.paymentStatus = com.smartparking.model.PaymentStatus.PAID")
    Double calculateTotalRevenue();

    @Query("SELECT b FROM Booking b ORDER BY b.createdAt DESC")
    List<Booking> findRecentBookings(Pageable pageable);
}
