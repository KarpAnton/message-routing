package com.andersenlab.messagebroker.exception;

public class ProducerAlreadyExistsException extends RuntimeException {

    public ProducerAlreadyExistsException(String producer) {
        super("Producer with name " + producer + " already exists");
    }
}
