package com.mjvs.jgsp.dto;

public class PriceDTO {
    private double priceLine;
    private double priceZone;

    public PriceDTO() {

    }

    public PriceDTO(double priceLine, double priceZone) {
        this.priceLine = priceLine;
        this.priceZone = priceZone;
    }

    public double getPriceLine() {
        return priceLine;
    }

    public void setPriceLine(double priceLine) {
        this.priceLine = priceLine;
    }

    public double getPriceZone() {
        return priceZone;
    }

    public void setPriceZone(double priceZone) {
        this.priceZone = priceZone;
    }
}
