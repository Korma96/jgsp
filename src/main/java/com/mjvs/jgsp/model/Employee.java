package com.mjvs.jgsp.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Employee extends Passenger {
    @Column(name = "firm", unique = false, nullable = false)
    private String firm;

    public Employee(String username, String password, String firstName, String lastName, String email, String address, String firm) {
        super(username, password, firstName, lastName, email, address, PassengerType.EMPLOYEE);

        this.firm = firm;
    }

    public String getFirm() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }
}
