package com.mjvs.jgsp.dto;


public class DateTimesAndPriceDTO {
    private String startDateTime;
    private String endDateTime;
    private double price;

    public DateTimesAndPriceDTO() {

    }

    public DateTimesAndPriceDTO(String startDateTime, String endDateTime, double price) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.price = price;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
