package com.mjvs.jgsp.dto;

import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.TicketType;

import java.time.LocalDate;

public class PriceTicketDTO {
    private long id;
    private LocalDate dateFrom;
    private PassengerType passengerType;
    private TicketType ticketType;
    private double priceLine;
    private double priceZone;
    private String zone;

    public PriceTicketDTO(){

    }

    public PriceTicketDTO(long id, LocalDate dateFrom, PassengerType passengerType, TicketType ticketType, double priceLine, double priceZone, String zone) {
        this.id = id;
        this.dateFrom = dateFrom;
        this.passengerType = passengerType;
        this.ticketType = ticketType;
        this.priceLine = priceLine;
        this.priceZone = priceZone;
        this.zone = zone;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public PassengerType getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(PassengerType passengerType) {
        this.passengerType = passengerType;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
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

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
