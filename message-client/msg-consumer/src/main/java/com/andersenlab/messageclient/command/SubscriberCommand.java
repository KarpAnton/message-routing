package com.andersenlab.messageclient.command;

import com.andersenlab.client.utils.AddressUtils;
import com.andersenlab.messagebroker.controller.SubscriberControllerApi;
import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.pubsub.Subscriber;
import com.andersenlab.messageclient.context.SubscriberContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.LocalDateTime;

@ShellComponent
public class SubscriberCommand {

    @Autowired
    private SubscriberControllerApi subscriberControllerApi;

    @ShellMethod(key = "set-consumer", value = "Sets up consumer parameters into consumer context and sends them to broker")
    public void setConsumerParameters(@ShellOption({"-n", "-name"}) String subscriberName,
                                      @ShellOption("-dest") String destinationName) {

        Subscriber subscriber = new Subscriber();
        subscriber.setName(subscriberName);
        subscriber.setAddress(AddressUtils.extractCurAddress());
        subscriber.setDestination(MsgDestination.createDestination(destinationName));
        subscriber.setCreatedAt(LocalDateTime.now());

        SubscriberContext.setParameters(subscriber);

        subscriberControllerApi.subscribe(subscriber);
    }
}
