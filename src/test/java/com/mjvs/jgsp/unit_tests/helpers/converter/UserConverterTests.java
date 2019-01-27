package com.mjvs.jgsp.unit_tests.helpers.converter;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mjvs.jgsp.dto.DeactivatedPassengerDTO;
import com.mjvs.jgsp.dto.RequestDTO;
import com.mjvs.jgsp.dto.UserFrontendDTO;
import com.mjvs.jgsp.helpers.converter.UserConverter;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.User;
import com.mjvs.jgsp.model.UserStatus;
import com.mjvs.jgsp.model.UserType;

public class UserConverterTests {
	
	@Test
    public void ConvertUserToUserFrontendDTOsTest()
    {
        List<User> users = new ArrayList<User>()
        {{
            add(new User("username", "password",UserType.CONTROLLOR, UserStatus.ACTIVATED));
            add(new User("username2", "password2",UserType.PASSENGER, UserStatus.ACTIVATED));

        }};

        List<UserFrontendDTO> frontendDTOs = UserConverter.ConvertUserToUserFrontendDTOs(users);

        assertEquals(2, frontendDTOs.size());
        assertThat(frontendDTOs.get(0), isA(UserFrontendDTO.class));
        assertEquals(users.get(0).getUsername(), frontendDTOs.get(0).getUsername());
        assertEquals(users.get(1).getUserStatus(), frontendDTOs.get(1).getUserStatus());
        assertEquals(users.get(0).getUserType(), frontendDTOs.get(0).getUserType());
        assertNull(frontendDTOs.get(0).getId());
    }

	@Test
    public void ConvertPassengerToRequestDTOsTest()
    {
        List<Passenger> passengers = new ArrayList<Passenger>()
        {{
            add(new Passenger("username1", "password1", "name1", "lastname1", "email1", "address1", PassengerType.OTHER, 1L,LocalDate.now(), null));
            add(new Passenger("username2", "password2", "name2", "lastname2", "email2", "address2", PassengerType.STUDENT, 2L,LocalDate.now(), null));

        }};

        List<RequestDTO> requestDTOs = UserConverter.ConvertPassengerToRequestDTOs(passengers);

        assertEquals(2, requestDTOs.size());
        assertThat(requestDTOs.get(0), isA(RequestDTO.class));
        assertEquals(passengers.get(0).getFirstName(), requestDTOs.get(0).getFirstName());
        assertEquals(passengers.get(1).getIdConfirmation(), requestDTOs.get(1).getIdConfirmation());
        assertEquals(passengers.get(0).getPassengerType(), requestDTOs.get(0).getPassengerType());
        assertNull(requestDTOs.get(1).getId());
    }
	
	@Test
    public void ConvertUserToUserFrontendDTOsEmptyListTest()
    {

		List<Passenger> passengers = new ArrayList<>();

        List<RequestDTO> requestDTOs = UserConverter.ConvertPassengerToRequestDTOs(passengers);

        assertEquals(0, requestDTOs.size());
    }
	
	
	@Test
    public void ConvertPassengerToDeactivatedPassengerDTOsTest()
    {
        List<Passenger> passengers = new ArrayList<Passenger>()
        {{
            add(new Passenger("username1", "password1", "name1", "lastname1", "email1", "address1", PassengerType.OTHER, 1L,LocalDate.now(), null));
            add(new Passenger("username2", "password2", "name2", "lastname2", "email2", "address2", PassengerType.STUDENT, 2L,LocalDate.now(), null));

        }};

        List<DeactivatedPassengerDTO> DeactivatedPassengerDTOs = UserConverter.ConvertPassengerToDeactivatedPassengerDTO(passengers);


        assertEquals(2, DeactivatedPassengerDTOs.size());
        assertThat(DeactivatedPassengerDTOs.get(0), isA(DeactivatedPassengerDTO.class));
        assertEquals(passengers.get(0).getFirstName(), DeactivatedPassengerDTOs.get(0).getFirstName());
        assertEquals(passengers.get(1).getEmail(), DeactivatedPassengerDTOs.get(1).getEmail());
        assertEquals(passengers.get(0).getPassengerType(), DeactivatedPassengerDTOs.get(0).getPassengerType());
        assertNull(DeactivatedPassengerDTOs.get(1).getId());
    }
}
