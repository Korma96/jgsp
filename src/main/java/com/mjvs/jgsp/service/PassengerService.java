package com.mjvs.jgsp.service;

import com.mjvs.jgsp.dto.PassengerDTO;
import com.mjvs.jgsp.dto.TicketFrontendDTO;
import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.helpers.exception.UserNotFoundException;
import com.mjvs.jgsp.model.*;

import java.time.LocalDateTime;
import java.util.List;


public interface PassengerService {
	
    Passenger save(Passenger p);

    Passenger getPassenger(String username);
    
    Ticket buyTicket(boolean hasZoneNotLine, String lineZoneName, int dayInMonthOrMonthInYear, TicketType ticketType)
            throws Exception;

    boolean registrate(String username, String password1, String Password2, String firstName, String lastName, String email, String address);

    LineZone getLineZone(boolean hasZoneNotLine, String lineZoneName, TicketType ticketType) throws Exception;

    LocalDateTime[] createStartAndEndDateTime(TicketType ticketType, PassengerType passengerType, int dayInMonthOrMonthInYear)
            throws BadRequestException;

    List<TicketFrontendDTO> getTickets() throws UserNotFoundException;
}