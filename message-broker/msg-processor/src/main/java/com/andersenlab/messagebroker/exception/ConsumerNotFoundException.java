package com.andersenlab.messagebroker.exception;

public class ConsumerNotFoundException extends RuntimeException {

    public ConsumerNotFoundException(String consumerName) {
        super("Consumer " + consumerName + " not found");
    }
}
