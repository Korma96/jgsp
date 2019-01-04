package com.mjvs.jgsp.helpers.converter;


import com.mjvs.jgsp.dto.TicketFrontendDTO;
import com.mjvs.jgsp.model.Ticket;

import java.util.List;
import java.util.stream.Collectors;

public class TicketConverter {

    public static List<TicketFrontendDTO> ConvertTicketsToTicketFrontendDTOs(List<Ticket> tickets)
    {
        return tickets.stream()
                .map(ticket -> new TicketFrontendDTO(ticket))
                .collect(Collectors.toList());
    }
}
