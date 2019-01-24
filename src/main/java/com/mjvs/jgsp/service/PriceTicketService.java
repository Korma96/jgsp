package com.mjvs.jgsp.service;

import com.mjvs.jgsp.dto.PriceTicketDTO;
import com.mjvs.jgsp.dto.PriceTicketFrontendDTO;
import com.mjvs.jgsp.helpers.exception.PriceTicketNotFoundException;
import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.PriceTicket;
import com.mjvs.jgsp.model.TicketType;
import com.mjvs.jgsp.model.Zone;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public interface PriceTicketService {
	
	PriceTicket getLatestPriceTicket(PassengerType passengerType, TicketType ticketType, Zone zone) throws PriceTicketNotFoundException;

	void save(PriceTicket priceTicket);

	Map addTicket(PriceTicketDTO priceTicketDTO);

	List<PriceTicketFrontendDTO> getAllPriceTickets();

	List<PriceTicket> getAll();

	Map<String,String> delete(Long id);

	Pair update(PriceTicketDTO priceTicketDTO, Long id);

}