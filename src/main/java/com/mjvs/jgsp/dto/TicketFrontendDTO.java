package com.mjvs.jgsp.dto;

import com.mjvs.jgsp.model.LineZone;
import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.Ticket;
import com.mjvs.jgsp.model.TicketType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TicketFrontendDTO {
    private Long id;
    private String startDateAndTime;
    private String endDateAndTime;
    private String ticketType;
    private double price;
    private String passengerType;
    private String lineZone;


    public TicketFrontendDTO() {

    }

    public TicketFrontendDTO(Long id, String startDateAndTime, String endDateAndTime, String ticketType, double price, String passengerType, String lineZone) {
        this.id = id;
        this.startDateAndTime = startDateAndTime;
        this.endDateAndTime = endDateAndTime;
        this.ticketType = ticketType;
        this.price = price;
        this.passengerType = passengerType;
        this.lineZone = lineZone;
    }

    public TicketFrontendDTO(Ticket ticket) {
        this.id = ticket.getId();
        this.startDateAndTime = ticket.getStartDateAndTimeStr();
        this.endDateAndTime = ticket.getEndDateAndTimeStr();
        this.ticketType = ticket.getTicketType().name().toLowerCase();
        this.price = ticket.getPrice();
        this.passengerType = ticket.getPassengerType().name().toLowerCase();
        this.lineZone = ticket.getLineZoneCompleteName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartDateAndTime() {
        return startDateAndTime;
    }

    public void setStartDateAndTime(String startDateAndTime) {
        this.startDateAndTime = startDateAndTime;
    }

    public String getEndDateAndTime() {
        return endDateAndTime;
    }

    public void setEndDateAndTime(String endDateAndTime) {
        this.endDateAndTime = endDateAndTime;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public String getLineZone() {
        return lineZone;
    }

    public void setLineZone(String lineZone) {
        this.lineZone = lineZone;
    }
}
