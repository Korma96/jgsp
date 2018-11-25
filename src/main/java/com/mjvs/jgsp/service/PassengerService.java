package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.helpers.exception.UserNotFoundException;
import com.mjvs.jgsp.model.Passenger;
import com.mjvs.jgsp.model.TicketType;


public interface PassengerService {

    void buyTicket(boolean hasZoneNotLine, Long id, int dayInMonthOrMonthInYear, TicketType ticketType)
            throws Exception;

}