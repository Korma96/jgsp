package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.exception.TicketNotFoundException;
import com.mjvs.jgsp.model.Ticket;

import java.io.ByteArrayInputStream;

public interface TicketService {
    boolean checkOnetimeTicket(Long ticketId, Long lineId) throws Exception;
    
    Ticket getTicket(Long id);

    Ticket save(Ticket ticket);

    ByteArrayInputStream getPdfFileForTicket(Long id) throws TicketNotFoundException;
}
