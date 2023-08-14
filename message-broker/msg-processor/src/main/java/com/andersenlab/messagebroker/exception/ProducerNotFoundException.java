package com.andersenlab.messagebroker.exception;

public class ProducerNotFoundException extends RuntimeException {

    public ProducerNotFoundException(String publisher) {
        super("Producer " + publisher + " not registered");
    }
}
