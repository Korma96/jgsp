package com.mjvs.jgsp.dto;

import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.UserStatus;

public class DeactivatedPassengerDTO {
	private Long id;
	private String username;
    private UserStatus userStatus;
    private PassengerType passengerType;
    private String firstName;
    private String lastName;
    private String email;
    
    public DeactivatedPassengerDTO(Passenger passenger) {

		this.id = passenger.getId();
		this.username = passenger.getEmail();
		this.userStatus = passenger.getUserStatus();
		this.email = passenger.getEmail();
		this.firstName = passenger.getFirstName();
		this.lastName = passenger.getLastName();
		this.passengerType = passenger.getPassengerType();
	}

	public DeactivatedPassengerDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public PassengerType getPassengerType() {
		return passengerType;
	}

	public void setPassengerType(PassengerType passengerType) {
		this.passengerType = passengerType;
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
    
    

}
