package com.mjvs.jgsp.helpers.exception;

public class ZoneNotFoundException extends Exception {

    public ZoneNotFoundException() {
        super("Zone was not found in database.");
    }

    public ZoneNotFoundException(String message) {
        super(message);
    }
}
