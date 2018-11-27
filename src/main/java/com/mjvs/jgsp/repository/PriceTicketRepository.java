package com.mjvs.jgsp.repository;

import org.springframework.data.repository.Repository;

import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.PriceTicket;
import com.mjvs.jgsp.model.TicketType;
import com.mjvs.jgsp.model.Zone;

public interface PriceTicketRepository extends Repository<PriceTicket, Long> {
	
	PriceTicket findTopByPassengerTypeAndTicketTypeAndZoneOrderByDateFromDesc(PassengerType passengerType, TicketType ticketType, 
																			Zone zone);
	
	PriceTicket save(PriceTicket priceTicket);
}
