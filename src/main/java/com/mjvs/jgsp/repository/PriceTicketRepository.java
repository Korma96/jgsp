package com.mjvs.jgsp.repository;

import com.mjvs.jgsp.model.*;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface PriceTicketRepository extends Repository<PriceTicket, Long> {
	
	PriceTicket findTopByPassengerTypeAndTicketTypeAndZoneOrderByDateFromDesc(PassengerType passengerType, TicketType ticketType, 
																			Zone zone);
	
	PriceTicket save(PriceTicket priceTicket);

	List<PriceTicket> findAll();

	PriceTicket findById(Long id);

	//PriceTicket findPriceTicketByPassengerTypeAndTicketTypeAndZone(PassengerType passengerType, TicketType ticketType,Zone zone);
}
