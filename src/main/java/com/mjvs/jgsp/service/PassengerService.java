package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.exception.*;
import com.mjvs.jgsp.model.*;
import org.springframework.web.multipart.MultipartFile;

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

    List<Ticket> getTickets() throws UserNotFoundException;

    double getPrice(boolean hasZoneNotLine, TicketType ticketType, String zoneName) throws UserNotFoundException, ZoneNotFoundException, PriceTicketNotFoundException;

    void changeAccountType(PassengerType newPassengerType, MultipartFile image) throws Exception;

    void removeTicket(Long id) throws UserNotFoundException, TicketNotFoundException, CanNotBeDeletedException;
    
    List <Passenger> getAll();

	List<Passenger> getRequests();

}