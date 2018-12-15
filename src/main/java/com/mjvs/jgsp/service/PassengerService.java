package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.model.*;

import java.time.LocalDateTime;


public interface PassengerService {
	
    Passenger save(Passenger p);

    Passenger getPassenger(String username);
    
    Ticket buyTicket(boolean hasZoneNotLine, Long lineZoneId, int dayInMonthOrMonthInYear, TicketType ticketType)
            throws Exception;

    LineZone getLineZone(boolean hasZoneNotLine, Long lineZoneId, TicketType ticketType) throws Exception;

    LocalDateTime[] createStartAndEndDateTime(TicketType ticketType, PassengerType passengerType, int dayInMonthOrMonthInYear)
            throws BadRequestException;

}