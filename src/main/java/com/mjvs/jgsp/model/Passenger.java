package com.mjvs.jgsp.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Passenger extends User {
    @Column(name = "first_name", unique = false, nullable = false)
    private String firstName;

    @Column(name = "last_name", unique = false, nullable = false)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "address", unique = false, nullable = false)
    private String address;

    @Column(name = "num_of_delicts", unique = false, nullable = true)
    private int numOfDelicts;

    @Column(name = "passenger_type", unique = false, nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private PassengerType passengerType;

    @Column(name = "new_passenger_type", unique = false, nullable = true)
    @Enumerated(EnumType.ORDINAL)
    private PassengerType newPassengerType;

    @Column(name = "id_confirmation", unique = false, nullable = true)
    private Long idConfirmation;

    @Column(name = "expiration_date", unique = false, nullable = true)
    private LocalDate expirationDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private User verifiedBy;

    public Passenger() {

    }

    public Passenger(String username, String password, String firstName, String lastName, String email, String address, PassengerType passengerType, Long idConfirmation, LocalDate expirationDate, User verifiedBy) {
        super(username, password, UserType.PASSENGER, UserStatus.ACTIVATED);

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.passengerType = passengerType;
        this.idConfirmation = idConfirmation;
        this.expirationDate = expirationDate;
        this.tickets = new ArrayList<Ticket>();
        this.verifiedBy = verifiedBy;
        this.numOfDelicts = 0;
    }


    public Passenger(String username, String password, UserType userType, UserStatus userStatus, String firstName, String lastName, String email, String address, PassengerType passengerType) {
        super(username, password, userType, userStatus);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.passengerType = passengerType;
        this.numOfDelicts = 0;
        this.tickets = new ArrayList<Ticket>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PassengerType getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(PassengerType passengerType) {
        this.passengerType = passengerType;
    }

    public Long getIdConfirmation() { return idConfirmation; }

    public void setIdConfirmation(Long idConfirmation) { this.idConfirmation = idConfirmation; }

    public LocalDate getExpirationDate() { return expirationDate; }

    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    public List<Ticket> getTickets() { return tickets; }

    public void setTickets(List<Ticket> tickets) { this.tickets = tickets; }

    public User getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(User verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public int getNumOfDelicts() { return numOfDelicts; }

    public void setNumOfDelicts(int numOfDelicts) { this.numOfDelicts = numOfDelicts;}


    public boolean containsTicket(Ticket ticket) {
        return tickets.stream()
                .filter(t -> t.equals(ticket))
                .count() > 0;
    }

    public PassengerType getNewPassengerType() {
        return newPassengerType;
    }

    public void setNewPassengerType(PassengerType newPassengerType) {
        this.newPassengerType = newPassengerType;
    }
}