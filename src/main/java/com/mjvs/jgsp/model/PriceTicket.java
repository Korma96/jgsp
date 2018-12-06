package com.mjvs.jgsp.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class PriceTicket extends EntityForDeleted {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name="date_from", unique=false, nullable=false)
    private LocalDate dateFrom;

    @Column(name="passenger_type", unique=false, nullable=false)
    @Enumerated(EnumType.ORDINAL)
    private PassengerType passengerType;

    @Column(name="ticket_type", unique=false, nullable=false)
    @Enumerated(EnumType.ORDINAL)
    private TicketType ticketType;

    @Column(name="priceLine", unique=false, nullable=false)
    private double priceLine;

    @Column(name="priceZone", unique=false, nullable=false)
    private double priceZone;

    // nullable se koristi kao ogranicenje nad semom, optinal se koristi u runtime-u, pri proveri, pre kontakta sa bazom
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Zone zone;

    public PriceTicket() {

    }

    public PriceTicket(LocalDate dateFrom, PassengerType passengerType, TicketType ticketType, double priceLine, double priceZone, Zone zone) {
        super();
        this.dateFrom = dateFrom;
        this.passengerType = passengerType;
        this.ticketType = ticketType;
        this.priceLine = priceLine;
        this.priceZone = priceZone;
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

    public double getPriceZone() { return priceZone; }

    public void setPriceZone(double priceZone) {this.priceZone = priceZone; }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }


    public double getPriceLine() { return priceLine; }

    public void setPriceLine(double priceLine) { this.priceLine = priceLine; }
}
