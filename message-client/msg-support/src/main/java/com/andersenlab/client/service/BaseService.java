package com.andersenlab.client.service;

import com.andersenlab.client.context.ClientContext;
import com.andersenlab.messagebroker.pubsub.Client;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseService<T extends Client> {
    @Autowired
    protected ClientContext<T> publisherContext;
}
