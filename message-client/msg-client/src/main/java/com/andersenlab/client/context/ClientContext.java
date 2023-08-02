package com.andersenlab.client.context;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.pubsub.Client;

public interface ClientContext<T extends Client> {

    T getClient();
    void setClient(T client);
    MsgDestination getDestination();
    String getName();
    String getAddress();
}
