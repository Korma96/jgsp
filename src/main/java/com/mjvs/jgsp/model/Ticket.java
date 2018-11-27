package com.mjvs.jgsp.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name="start_date_and_time", unique=false, nullable=true)
    private LocalDateTime startDateAndTime;

    @Column(name="end_date_and_time", unique=false, nullable=true)
    private LocalDateTime endDateAndTime;

    @Column(name = "ticket_type", unique = false, nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TicketType ticketType;

    @Column(name = "price", unique = false, nullable = false)
    private double price;

    @Column(name = "activated", unique = false, nullable = false)
    private boolean activated;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private LineZone lineZone;

    /*@ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Line line;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Zone zone;*/

    public Ticket() {

    }

    public Ticket(LocalDateTime startDateAndTime, LocalDateTime endDateAndTime, TicketType ticketType, boolean activated, LineZone lineZone/*, Line line, Zone zone*/) {
        this.startDateAndTime = startDateAndTime;
        this.endDateAndTime = endDateAndTime;
        this.ticketType = ticketType;
        this.price = 0;
        this.activated = activated;
        this.lineZone = lineZone;
        //this.line = line;
        //this.zone = zone;
    }

    public void lookAtPriceTicketAndSetPrice(PriceTicket priceTicket) {
        this.price = lineZone.getPrice(priceTicket);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartDateAndTime() {
        return startDateAndTime;
    }

    public void setStartDateAndTime(LocalDateTime startDateAndTime) {
        this.startDateAndTime = startDateAndTime;
    }

    public LocalDateTime getEndDateAndTime() {
        return endDateAndTime;
    }

    public void setEndDateAndTime(LocalDateTime endDateAndTime) {
        this.endDateAndTime = endDateAndTime;
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

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public LineZone getLineZone() {
        return lineZone;
    }

    public void setLineZone(LineZone lineZone) {
        this.lineZone = lineZone;
    }

    /*public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }*/
}
