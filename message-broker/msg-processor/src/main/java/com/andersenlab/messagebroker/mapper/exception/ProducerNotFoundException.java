package com.andersenlab.messagebroker.mapper.exception;

public class ProducerNotFoundException extends RuntimeException {

    public ProducerNotFoundException() {
    }

    public ProducerNotFoundException(String message) {
        super(message);
    }

    public ProducerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
