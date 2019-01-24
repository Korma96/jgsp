package com.mjvs.jgsp.helpers.converter;

import java.util.List;
import java.util.stream.Collectors;

import com.mjvs.jgsp.dto.DeactivatedPassengerDTO;
import com.mjvs.jgsp.dto.RequestDTO;
import com.mjvs.jgsp.dto.UserFrontendDTO;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.User;

public class UserConverter {

	public static List<UserFrontendDTO> ConvertUserToUserFrontendDTOs(List<User> users) {
		return users.stream().map(user -> new UserFrontendDTO(user)).collect(Collectors.toList());
	}

	public static List<RequestDTO> ConvertPassengerToRequestDTOs(List<Passenger> passengers) {
		return passengers.stream().map(passenger -> new RequestDTO(passenger)).collect(Collectors.toList());
	}

	public static List<DeactivatedPassengerDTO> ConvertPassengerToDeactivatedPassengerDTO(List<Passenger> passengers) {
		return passengers.stream().map(passenger -> new DeactivatedPassengerDTO(passenger)).collect(Collectors.toList());
	}
}
