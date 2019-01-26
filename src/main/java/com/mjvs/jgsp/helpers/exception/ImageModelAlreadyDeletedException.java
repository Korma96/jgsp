package com.mjvs.jgsp.helpers.exception;

public class ImageModelAlreadyDeletedException extends Exception {

    public ImageModelAlreadyDeletedException() {
        super("ImageModel is already deleted.");
    }

    public ImageModelAlreadyDeletedException(String message) {
        super(message);
    }
}
