package com.andersenlab.messagebroker.mapper.exception;

public class DestinationNotFoundException extends RuntimeException {
    public DestinationNotFoundException() {
    }

    public DestinationNotFoundException(String message) {
        super(message);
    }

    public DestinationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}