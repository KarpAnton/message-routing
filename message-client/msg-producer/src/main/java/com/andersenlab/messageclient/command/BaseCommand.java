package com.andersenlab.messageclient.command;

import com.andersenlab.client.context.ClientContext;
import com.andersenlab.messagebroker.pubsub.Publisher;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseCommand {

    @Autowired
    protected ClientContext<Publisher> publisherContext;
}
