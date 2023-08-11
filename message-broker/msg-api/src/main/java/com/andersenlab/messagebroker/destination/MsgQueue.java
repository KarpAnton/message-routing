package com.andersenlab.messagebroker.destination;

public class MsgQueue extends MsgDestination {

    public MsgQueue() {
        super(DestinationType.QUEUE);
    }

    public MsgQueue(String name) {
        super(name, DestinationType.QUEUE);
    }
}
