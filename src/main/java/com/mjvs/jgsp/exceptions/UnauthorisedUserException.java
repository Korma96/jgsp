package com.mjvs.jgsp.exceptions;

@SuppressWarnings("serial")
public class UnauthorisedUserException extends Exception {

	public UnauthorisedUserException() {
        super("Unauthorised user exception");
    }

    public UnauthorisedUserException(String message) {
        super(message);
    }
}
