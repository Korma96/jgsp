package com.mjvs.jgsp.helpers.exception;

public class TicketNotFoundException extends Exception {

    public TicketNotFoundException() {
        super("Ticket was not found in database.");
    }

    public TicketNotFoundException(String message) {
        super(message);
    }
}
