package com.mjvs.jgsp.helpers.exception;

public class ImageModelAlreadyDeleted extends Exception {

    public ImageModelAlreadyDeleted() {
        super("ImageModel is already deleted.");
    }

    public ImageModelAlreadyDeleted(String message) {
        super(message);
    }
}
