package com.mjvs.jgsp.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Pensioner extends Passenger {
    @Column(name = "pension_and_disability_insurance", unique = false, nullable = false)
    private String pensionAndDisabilityInsurance;

    public Pensioner(String username, String password, String firstName, String lastName, String email, String address, String pensionAndDisabilityInsurance) {
        super(username, password, firstName, lastName, email, address, PassengerType.PENSIONER);

        this.pensionAndDisabilityInsurance = pensionAndDisabilityInsurance;
    }

    public String getPensionAndDisabilityInsurance() {
        return pensionAndDisabilityInsurance;
    }

    public void setPensionAndDisabilityInsurance(String pensionAndDisabilityInsurance) {
        this.pensionAndDisabilityInsurance = pensionAndDisabilityInsurance;
    }
}
