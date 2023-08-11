package com.andersenlab.client.context;


import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.pubsub.Client;

public class DefaultClientContext<T extends Client> implements ClientContext<T> {

    protected T client;

    public MsgDestination getDestination() {
        return client.getDestination();
    }

    public String getName() {
        return client.getName();
    }

    public String getAddress() {
        return client.getAddress();
    }

    @Override
    public T getClient() {
        return client;
    }

    @Override
    public void setClient(T client) {
        this.client = client;
    }
}
