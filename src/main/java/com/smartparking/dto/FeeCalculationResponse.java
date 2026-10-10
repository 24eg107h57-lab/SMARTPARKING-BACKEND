package com.smartparking.dto;

public class FeeCalculationResponse {
    private Double hourlyRate;
    private Double hours;
    private Double baseFee;
    private Double taxAmount;
    private Double totalFee;

    public FeeCalculationResponse() {}

    public FeeCalculationResponse(Double hourlyRate, Double hours, Double baseFee, Double taxAmount, Double totalFee) {
        this.hourlyRate = hourlyRate;
        this.hours = hours;
        this.baseFee = baseFee;
        this.taxAmount = taxAmount;
        this.totalFee = totalFee;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public Double getBaseFee() {
        return baseFee;
    }

    public void setBaseFee(Double baseFee) {
        this.baseFee = baseFee;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }
}
