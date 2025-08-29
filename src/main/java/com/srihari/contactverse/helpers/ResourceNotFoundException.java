package com.srihari.contactverse.helpers;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {

        super(message);
    }

    public ResourceNotFoundException() {
        super("resource not found");
    }

}
