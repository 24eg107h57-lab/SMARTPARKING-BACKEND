package com.smartparking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "parking_locations")
public class ParkingLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Double carHourlyRate = 50.0;

    @Column(nullable = false)
    private Double bikeHourlyRate = 20.0;

    @Column(nullable = false)
    private Double evHourlyRate = 60.0;

    @Column(nullable = false)
    private Double truckHourlyRate = 100.0;

    @Column(nullable = false)
    private Integer totalSlots = 0;

    @Column(length = 255)
    private String imageUrl;

    public ParkingLocation() {
    }

    public ParkingLocation(String name, String address, String city, String description,
                           Double carHourlyRate, Double bikeHourlyRate, Double evHourlyRate, Double truckHourlyRate,
                           Integer totalSlots, String imageUrl) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.description = description;
        this.carHourlyRate = carHourlyRate;
        this.bikeHourlyRate = bikeHourlyRate;
        this.evHourlyRate = evHourlyRate;
        this.truckHourlyRate = truckHourlyRate;
        this.totalSlots = totalSlots;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCarHourlyRate() {
        return carHourlyRate;
    }

    public void setCarHourlyRate(Double carHourlyRate) {
        this.carHourlyRate = carHourlyRate;
    }

    public Double getBikeHourlyRate() {
        return bikeHourlyRate;
    }

    public void setBikeHourlyRate(Double bikeHourlyRate) {
        this.bikeHourlyRate = bikeHourlyRate;
    }

    public Double getEvHourlyRate() {
        return evHourlyRate;
    }

    public void setEvHourlyRate(Double evHourlyRate) {
        this.evHourlyRate = evHourlyRate;
    }

    public Double getTruckHourlyRate() {
        return truckHourlyRate;
    }

    public void setTruckHourlyRate(Double truckHourlyRate) {
        this.truckHourlyRate = truckHourlyRate;
    }

    public Integer getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(Integer totalSlots) {
        this.totalSlots = totalSlots;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
