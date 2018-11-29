package com.mjvs.jgsp.service;

import com.mjvs.jgsp.helpers.exception.LineNotFoundException;
import com.mjvs.jgsp.helpers.exception.TicketNotFoundException;
import com.mjvs.jgsp.helpers.exception.UserNotFoundException;
import com.mjvs.jgsp.model.Ticket;

import java.io.ByteArrayInputStream;

public interface TicketService {
    boolean checkOnetimeTicket(Long ticketId, Long lineId) throws TicketNotFoundException, LineNotFoundException, UserNotFoundException;
    
    Ticket getTicket(Long id);

    ByteArrayInputStream getPdfFileForTicket(Long id) throws TicketNotFoundException;
}
