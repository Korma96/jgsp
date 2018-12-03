package com.mjvs.jgsp.repository;

import com.mjvs.jgsp.model.Ticket;
import org.springframework.data.repository.Repository;

public interface TicketRepository extends Repository<Ticket, Long> {

    Ticket findById(Long id);

    Ticket save(Ticket ticket);
}
