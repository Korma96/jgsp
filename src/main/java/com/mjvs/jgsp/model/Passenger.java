package com.mjvs.jgsp.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Passenger extends User {
    @Column(name = "first_name", unique = false, nullable = false)
    private String firstName;

    @Column(name = "last_name", unique = false, nullable = false)
    private String lastName;

    @Column(name = "email", unique = false, nullable = false)
    private String email;

    @Column(name = "address", unique = false, nullable = false)
    private String address;

    @Column(name = "passenger_type", unique = false, nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private PassengerType passengerType;

    public Passenger() {

    }

    public Passenger(String username, String password, String firstName, String lastName, String email, String address, PassengerType passengerType) {
        super(username, password, UserType.PASSENGER, UserStatus.ACTIVATED);

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.passengerType = passengerType;
    }


}
