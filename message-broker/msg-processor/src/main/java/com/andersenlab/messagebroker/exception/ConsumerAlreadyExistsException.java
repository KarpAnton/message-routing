package com.andersenlab.messagebroker.exception;

public class ConsumerAlreadyExistsException extends RuntimeException {
    public ConsumerAlreadyExistsException() {
    }

    public ConsumerAlreadyExistsException(String message) {
        super(message);
    }
}
