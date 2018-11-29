package com.mjvs.jgsp.helpers.exception;

public class PriceTicketNotFoundException extends Exception {

    public PriceTicketNotFoundException() {
        super("PriceTicket was not found in database.");
    }

    public PriceTicketNotFoundException(String message) {
        super(message);
    }
}
