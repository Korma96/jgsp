package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.exception.TicketNotFoundException;
import com.mjvs.jgsp.model.Ticket;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface TicketService {
    boolean checkOnetimeTicket(Long ticketId, Long lineId) throws Exception;
    
    Ticket getTicket(Long id);
    
    List<Ticket> getAll();

    Ticket save(Ticket ticket);

    ByteArrayInputStream getPdfFileForTicket(Long id) throws TicketNotFoundException;
}
