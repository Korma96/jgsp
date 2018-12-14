package com.mjvs.jgsp.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Ticket extends EntityForDeleted {
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

    @Column(name="passenger_type", unique=false, nullable=false)
    @Enumerated(EnumType.ORDINAL)
    private PassengerType passengerType;

    //@Column(name = "activated", unique = false, nullable = false)
    //private boolean activated;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private LineZone lineZone;


    public Ticket() {

    }

    public Ticket(LocalDateTime startDateAndTime, LocalDateTime endDateAndTime, TicketType ticketType, PassengerType passengerType, /*boolean activated,*/ LineZone lineZone) {
        super();
        this.startDateAndTime = startDateAndTime;
        this.endDateAndTime = endDateAndTime;
        this.ticketType = ticketType;
        this.passengerType = passengerType;
        this.price = 0;
        //this.activated = activated;
        this.lineZone = lineZone;
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

    public PassengerType getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(PassengerType passengerType) {
        this.passengerType = passengerType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /*
    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }*/

    public LineZone getLineZone() {
        return lineZone;
    }

    public void setLineZone(LineZone lineZone) {
        this.lineZone = lineZone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Double.compare(ticket.price, price) == 0 &&
                Objects.equals(id, ticket.id) &&
                Objects.equals(startDateAndTime, ticket.startDateAndTime) &&
                Objects.equals(endDateAndTime, ticket.endDateAndTime) &&
                ticketType == ticket.ticketType &&
                passengerType == ticket.passengerType &&
                Objects.equals(lineZone, ticket.lineZone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDateAndTime, endDateAndTime, ticketType, price, passengerType, lineZone);
    }
}
