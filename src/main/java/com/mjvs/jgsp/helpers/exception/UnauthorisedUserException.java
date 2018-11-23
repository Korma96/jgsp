package com.mjvs.jgsp.helpers.exception;

@SuppressWarnings("serial")
public class UnauthorisedUserException extends Exception {

	public UnauthorisedUserException() {
        super("Unauthorised user exception");
    }

    public UnauthorisedUserException(String message) {
        super(message);
    }
}
