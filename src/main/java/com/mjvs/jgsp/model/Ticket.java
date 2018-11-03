package com.mjvs.jgsp.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name="start_date_and_time", unique=false, nullable=false)
    private LocalDateTime startDateAndTime;

    @Column(name="end_date_and_time", unique=false, nullable=false)
    private LocalDateTime endDateAndTime;

    @Column(name = "ticket_type", unique = false, nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TicketType ticketType;

    @Column(name = "price", unique = false, nullable = false)
    private double price;

    @Column(name = "activated", unique = false, nullable = false)
    private boolean activated;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Zone zone;
}
