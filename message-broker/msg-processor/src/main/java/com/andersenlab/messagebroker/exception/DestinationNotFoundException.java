package com.andersenlab.messagebroker.exception;

public class DestinationNotFoundException extends RuntimeException {

    public DestinationNotFoundException(String destination) {
        super("Destination " + destination + " not found");
    }
}
