package com.mjvs.jgsp.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class PriceTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name="date_from", unique=false, nullable=false)
    private LocalDate dateFrom;

    @Column(name="passenger_type", unique=false, nullable=false)
    private PassengerType passengerType;

    @Column(name="ticket_type", unique=false, nullable=false)
    private TicketType ticketType;

    @Column(name="price", unique=false, nullable=false)
    private double price;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Zone zone;

    public PriceTicket(LocalDate dateFrom, PassengerType passengerType, TicketType ticketType, double price, Zone zone) {
        this.dateFrom = dateFrom;
        this.passengerType = passengerType;
        this.ticketType = ticketType;
        this.price = price;
        this.zone = zone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }
}
