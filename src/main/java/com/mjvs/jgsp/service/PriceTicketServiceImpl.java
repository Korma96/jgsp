package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.exception.PriceTicketNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.PriceTicket;
import com.mjvs.jgsp.model.TicketType;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.repository.PriceTicketRepository;

@Service
public class PriceTicketServiceImpl implements PriceTicketService {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private PriceTicketRepository priceTicketRepository;
	
	@Override
	public PriceTicket getLatestPriceTicket(PassengerType passengerType, TicketType ticketType, Zone zone) throws PriceTicketNotFoundException {
		PriceTicket priceTicket = priceTicketRepository.findTopByPassengerTypeAndTicketTypeAndZoneOrderByDateFromDesc(passengerType, ticketType, zone);
		if(priceTicket == null) {
			String message = "PriceTicket(passengerType="+passengerType.name()
					+", ticketType="+ticketType+", lineZoneId="+zone.getId()+") was not found in database.";
			logger.error(message);
			throw new PriceTicketNotFoundException(message);
		}

		return priceTicket;
	}
	
	@Override
	public void save(PriceTicket priceTicket) {
		priceTicketRepository.save(priceTicket);
	}

}
