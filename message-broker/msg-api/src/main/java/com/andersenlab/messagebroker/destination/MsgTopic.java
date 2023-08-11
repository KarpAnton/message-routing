package com.andersenlab.messagebroker.destination;

public class MsgTopic extends MsgDestination {

    public MsgTopic() {
        super(DestinationType.TOPIC);
    }

    public MsgTopic(String name) {
        super(name, DestinationType.TOPIC);
    }
}
