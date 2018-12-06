package com.mjvs.jgsp.helpers.exception;

@SuppressWarnings("serial")
public class ImageModelNotFoundException extends Exception {

    public ImageModelNotFoundException() {
        super("ImageModel was not found in database.");
    }

    public ImageModelNotFoundException(String message) {
        super(message);
    }
}
