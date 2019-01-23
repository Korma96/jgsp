package com.mjvs.jgsp.dto;


import com.mjvs.jgsp.model.PriceTicket;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PriceTicketFrontendDTO {
    private long id;
    private String dateFrom;
    private String passengerType;
    private String ticketType;
    private double priceLine;
    private double priceZone;
    private String zone;

    public PriceTicketFrontendDTO(){

    }

    public PriceTicketFrontendDTO(long id, String dateFrom, String passengerType, String ticketType, double priceLine, double priceZone, String zone) {
        this.id = id;
        this.dateFrom = dateFrom;
        this.passengerType = passengerType;
        this.ticketType = ticketType;
        this.priceLine = priceLine;
        this.priceZone = priceZone;
        this.zone = zone;
    }


    public PriceTicketFrontendDTO(PriceTicket priceTicket) {
        this.id = priceTicket.getId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        this.dateFrom = priceTicket.getDateFrom().format(formatter);
        this.passengerType = priceTicket.getPassengerType().name();
        this.ticketType = priceTicket.getTicketType().name();
        this.priceLine = priceTicket.getPriceLine();
        this.priceZone = priceTicket.getPriceZone();
        this.zone = priceTicket.getZone().getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
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
