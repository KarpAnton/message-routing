package com.andersenlab.messagebroker.pubsub;

import com.andersenlab.messagebroker.destination.MsgDestination;

public class Subscriber {

    private String name;

    private String address;

    private MsgDestination destination;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MsgDestination getDestination() {
        return destination;
    }

    public void setDestination(MsgDestination destination) {
        this.destination = destination;
    }
}
