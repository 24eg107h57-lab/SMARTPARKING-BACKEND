package com.smartparking.config;

import com.smartparking.model.*;
import com.smartparking.repository.BookingRepository;
import com.smartparking.repository.ParkingLocationRepository;
import com.smartparking.repository.ParkingSlotRepository;
import com.smartparking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParkingLocationRepository locationRepository;

    @Autowired
    private ParkingSlotRepository slotRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Seed Users
        User admin = null;
        if (userRepository.findByUsername("admin").isEmpty()) {
            admin = new User(
                    "admin",
                    "admin@smartparking.com",
                    passwordEncoder.encode("admin123"),
                    "System Administrator",
                    "+91 9876543210",
                    Role.ROLE_ADMIN
            );
            userRepository.save(admin);
        } else {
            admin = userRepository.findByUsername("admin").get();
        }

        User user = null;
        if (userRepository.findByUsername("user").isEmpty()) {
            user = new User(
                    "user",
                    "user@smartparking.com",
                    passwordEncoder.encode("user123"),
                    "Alex Johnson",
                    "+91 9123456780",
                    Role.ROLE_USER
            );
            userRepository.save(user);
        } else {
            user = userRepository.findByUsername("user").get();
        }

        // Seed Locations & Slots if empty
        if (locationRepository.count() == 0) {
            ParkingLocation loc1 = new ParkingLocation(
                    "Downtown Metro Hub",
                    "104 MG Road, Central Business District",
                    "Bangalore",
                    "Prime multi-level smart parking facility equipped with fast EV chargers and automated barrier access.",
                    60.0, 25.0, 80.0, 150.0, 16,
                    "https://images.unsplash.com/photo-1506521781263-d8422e82f27a?auto=format&fit=crop&w=800&q=80"
            );
            locationRepository.save(loc1);

            ParkingLocation loc2 = new ParkingLocation(
                    "Airport Skyway Terminal",
                    "Terminal 2 Ground Deck, Chhatrapati Shivaji Int'l",
                    "Mumbai",
                    "Covered high-security terminal parking with 24/7 surveillance and direct covered shuttle access.",
                    100.0, 40.0, 130.0, 220.0, 16,
                    "https://images.unsplash.com/photo-1590674899484-d5640e854abe?auto=format&fit=crop&w=800&q=80"
            );
            locationRepository.save(loc2);

            ParkingLocation loc3 = new ParkingLocation(
                    "City Galleria Mall Plaza",
                    "Basement Level B1 & B2, Ring Road",
                    "Pune",
                    "Convenient retail parking with dedicated EV charging bays and motorcycle racks.",
                    50.0, 20.0, 70.0, 110.0, 12,
                    "https://images.unsplash.com/photo-1573348722427-f1d6819fdf98?auto=format&fit=crop&w=800&q=80"
            );
            locationRepository.save(loc3);

            ParkingLocation loc4 = new ParkingLocation(
                    "Tech Park Silicon Gate",
                    "Plot 42, Electronic City Phase 1",
                    "Bangalore",
                    "Corporate campus parking with smart sensors, wide lanes, and fleet management support.",
                    45.0, 15.0, 60.0, 90.0, 12,
                    "https://images.unsplash.com/photo-1545459720-aac8509eb02c?auto=format&fit=crop&w=800&q=80"
            );
            locationRepository.save(loc4);

            // Generate slots for Location 1 (Downtown Metro Hub)
            seedSlotsForLocation(loc1);
            // Generate slots for Location 2 (Airport Skyway)
            seedSlotsForLocation(loc2);
            // Generate slots for Location 3 (City Galleria)
            seedSlotsForLocation(loc3);
            // Generate slots for Location 4 (Tech Park)
            seedSlotsForLocation(loc4);

            // Seed a sample confirmed booking for demo user
            List<ParkingSlot> sampleSlots = slotRepository.findByLocationId(loc1.getId());
            if (!sampleSlots.isEmpty() && user != null) {
                ParkingSlot slot = sampleSlots.get(1); // Slot A-02
                slot.setStatus(SlotStatus.RESERVED);
                slotRepository.save(slot);

                Booking sampleBooking = new Booking(
                        "PRK-DEMO01",
                        user,
                        slot,
                        "KA-01-MJ-4050",
                        VehicleType.CAR,
                        LocalDateTime.now().minusHours(1),
                        LocalDateTime.now().plusHours(3),
                        slot.getEffectiveHourlyRate(),
                        4.0,
                        slot.getEffectiveHourlyRate() * 4.0,
                        PaymentStatus.PAID
                );
                sampleBooking.setQrTicketCode("TKT-PRK-DEMO01");
                bookingRepository.save(sampleBooking);
            }
        }
    }

    private void seedSlotsForLocation(ParkingLocation location) {
        String locCode = location.getName().substring(0, 1);

        // Floor 1 - Cars
        for (int i = 1; i <= 6; i++) {
            ParkingSlot slot = new ParkingSlot(
                    locCode + "1-" + String.format("%02d", i),
                    1,
                    "Zone A",
                    VehicleType.CAR,
                    (i == 3) ? SlotStatus.OCCUPIED : SlotStatus.AVAILABLE,
                    location,
                    location.getCarHourlyRate(),
                    false
            );
            slotRepository.save(slot);
        }

        // Floor 1 - EV Charging Slots
        for (int i = 1; i <= 3; i++) {
            ParkingSlot slot = new ParkingSlot(
                    locCode + "E-" + String.format("%02d", i),
                    1,
                    "EV Wing",
                    VehicleType.EV,
                    SlotStatus.AVAILABLE,
                    location,
                    location.getEvHourlyRate(),
                    true
            );
            slotRepository.save(slot);
        }

        // Floor 2 - Bikes
        for (int i = 1; i <= 4; i++) {
            ParkingSlot slot = new ParkingSlot(
                    locCode + "B-" + String.format("%02d", i),
                    2,
                    "Zone B",
                    VehicleType.BIKE,
                    SlotStatus.AVAILABLE,
                    location,
                    location.getBikeHourlyRate(),
                    false
            );
            slotRepository.save(slot);
        }

        // Floor 2 - Heavy / Trucks
        for (int i = 1; i <= 3; i++) {
            ParkingSlot slot = new ParkingSlot(
                    locCode + "T-" + String.format("%02d", i),
                    2,
                    "Zone C",
                    VehicleType.TRUCK,
                    (i == 2) ? SlotStatus.MAINTENANCE : SlotStatus.AVAILABLE,
                    location,
                    location.getTruckHourlyRate(),
                    false
            );
            slotRepository.save(slot);
        }
    }
}
