package com.mjvs.jgsp.dto;

import com.mjvs.jgsp.model.PassengerType;

public class PassengerDTO {
    private String username;
    private String password1;
    private String password2;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private PassengerType passengerType;


    public PassengerDTO(){

    }

    /*public PassengerDTO(Passenger p){
        this.username = p.getUsername();
        this.password1 = p.getPassword();
        this.password2 = p.getPassword();
        this.firstName = p.getFirstName();
        this.lastName = p.getLastName();
        this.email = p.getEmail();
        this.adress = p.getAddress();
        this.passengerType = p.getPassengerType();

    }*/

    public PassengerDTO(String username, String password1, String password2, String firstName, String lastName, String email, String address, PassengerType passengerType) {
        this.username = username;
        this.password1 = password1;
        this.password2 = password2;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.passengerType = passengerType;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
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
}
