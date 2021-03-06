package com.mjvs.jgsp.helpers.exception;

@SuppressWarnings("serial")
public class UserNotFoundException extends Exception {

	public UserNotFoundException() {
        super("User was not found in database.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
