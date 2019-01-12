package com.mjvs.jgsp.dto;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.PassengerType;

public class RequestDTO {

	private Long id;
	private String firstName;
	private String lastName;
	private String address;
	private PassengerType passengerType;
	private PassengerType newPassengerType;
	private Long idConfirmation;
	
	public RequestDTO() {
	}

	public RequestDTO(Passenger passenger) {

		this.id = passenger.getId();
		this.firstName = passenger.getFirstName();
		this.lastName = passenger.getLastName();
		this.address = passenger.getAddress();
		this.passengerType = passenger.getPassengerType();
		this.newPassengerType = passenger.getNewPassengerType();
		this.idConfirmation = passenger.getIdConfirmation();
	}

	public RequestDTO(Long id, String firstName, String lastName, String address,
			PassengerType passengerType, PassengerType newPassengerType, Long idConfirmation) {

		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.passengerType = passengerType;
		this.newPassengerType = newPassengerType;
		this.idConfirmation = idConfirmation;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public PassengerType getNewPassengerType() {
		return newPassengerType;
	}

	public void setNewPassengerType(PassengerType newPassengerType) {
		this.newPassengerType = newPassengerType;
	}

	public Long getIdConfirmation() {
		return idConfirmation;
	}

	public void setIdConfirmation(Long idConfirmation) {
		this.idConfirmation = idConfirmation;
	}

}
