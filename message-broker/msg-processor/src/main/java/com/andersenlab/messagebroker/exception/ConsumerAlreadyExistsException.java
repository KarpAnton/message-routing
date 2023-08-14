package com.andersenlab.messagebroker.exception;

public class ConsumerAlreadyExistsException extends RuntimeException {

    public ConsumerAlreadyExistsException(String consumer) {
        super("Consumer with name " + consumer + " already exists");
    }
}
