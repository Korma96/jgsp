package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.PriceTicket;
import com.mjvs.jgsp.model.TicketType;
import com.mjvs.jgsp.model.Zone;

public interface PriceTicketService {
	
	PriceTicket getPriceTicket(PassengerType passengerType, TicketType ticketType, Zone zone);

	void save(PriceTicket priceTicket);

}