package com.andersenlab.messagebroker.exception;

public class ProducerAlreadyExistsException extends RuntimeException {
    public ProducerAlreadyExistsException() {
    }

    public ProducerAlreadyExistsException(String message) {
        super(message);
    }
}
