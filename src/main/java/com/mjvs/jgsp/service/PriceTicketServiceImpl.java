package com.mjvs.jgsp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.PriceTicket;
import com.mjvs.jgsp.model.TicketType;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.repository.PriceTicketRepository;

@Service
public class PriceTicketServiceImpl implements PriceTicketService {

	@Autowired
	private PriceTicketRepository priceTicketRepository;
	
	@Override
	public PriceTicket getPriceTicket(PassengerType passengerType, TicketType ticketType, Zone zone) {
		return priceTicketRepository.findTopByPassengerTypeAndTicketTypeAndZoneOrderByDateFromDesc(passengerType, ticketType, zone); 
	}
	
	@Override
	public void save(PriceTicket priceTicket) {
		priceTicketRepository.save(priceTicket);
	}

}
