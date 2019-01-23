package com.mjvs.jgsp.service;

import com.mjvs.jgsp.dto.DateTimesAndPriceDTO;
import com.mjvs.jgsp.helpers.exception.TicketNotFoundException;
import com.mjvs.jgsp.model.Ticket;

import java.util.List;

public interface TicketService {
    DateTimesAndPriceDTO checkOnetimeTicket(Long ticketId) throws Exception;
    
    Ticket getTicket(Long id);
    
    List<Ticket> getAll();

    Ticket save(Ticket ticket);

    /*ByteArrayInputStream*/ byte[] getPdfFileForTicket(Long id) throws TicketNotFoundException;
}
