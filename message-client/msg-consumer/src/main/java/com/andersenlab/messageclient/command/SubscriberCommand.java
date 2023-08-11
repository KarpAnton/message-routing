package com.andersenlab.messageclient.command;

import com.andersenlab.client.context.ClientContext;
import com.andersenlab.client.utils.AddressUtils;
import com.andersenlab.messagebroker.controller.SubscriberControllerApi;
import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.pubsub.Subscriber;
import com.andersenlab.messageclient.listener.MessageListener;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.LocalDateTime;

@ShellComponent
public class SubscriberCommand {

    @Autowired
    private MessageListener messageListener;

    @Autowired
    private ClientContext<Subscriber> subscriberContext;

    @Autowired
    private SubscriberControllerApi subscriberControllerApi;

    @ShellMethod(key = "set-consumer", value = "Sets up consumer parameters into consumer context and sends them to broker")
    public void setConsumerParametersAndRun(@ShellOption({"--n", "--name"}) String subscriberName,
                                            @ShellOption("--dest") String destinationName) {

        Subscriber subscriber = new Subscriber();
        subscriber.setName(subscriberName);
        subscriber.setAddress(AddressUtils.extractCurAddress());
        subscriber.setDestination(MsgDestination.createDestination(destinationName, null));
        subscriber.setCreatedAt(LocalDateTime.now());

        subscriberContext.setClient(subscriber);

        subscriberControllerApi.subscribe(subscriber);

        runMessageListener();
    }

    @ShellMethod(key = "set-autocommit", value = "Sets auto commit property")
    public void setAutoCommitProperty(@ShellOption({"--v"}) String boolVal) {
        boolean autoCommit = BooleanUtils.toBoolean(boolVal);
        messageListener.setEnableAutoCommit(autoCommit);
    }

    @ShellMethod(key = "stop", value = "Stops a listener from getting messages")
    public void stopListener() {
        messageListener.setRunnable(false);
    }

    @ShellMethod(key = "start", value = "Starts a listener configured in the context")
    public void runListener() {
        messageListener.setRunnable(true);
        runMessageListener();
    }

    private void runMessageListener() {
        new Thread(messageListener).start();
    }
}
