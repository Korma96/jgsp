package com.mjvs.jgsp.helpers.exception;

@SuppressWarnings("serial")
public class LineNotFoundException extends Exception {

    public LineNotFoundException() {
        super("Line was not found in database.");
    }

    public LineNotFoundException(String message) {
        super(message);
    }
}
