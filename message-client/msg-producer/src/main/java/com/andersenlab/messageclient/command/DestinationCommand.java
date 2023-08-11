package com.andersenlab.messageclient.command;

import com.andersenlab.messagebroker.controller.BrokerControllerApi;
import com.andersenlab.messagebroker.destination.DestinationType;
import com.andersenlab.messagebroker.destination.MsgDestination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class DestinationCommand {

    private static final String QUEUE_PREFIX = "queue-";
    private static final String TOPIC_PREFIX = "topic-";

    @Autowired
    private BrokerControllerApi brokerControllerApi;

    @ShellMethod(key = "create-topic", value = "Creates topic in message broker")
    public String createTopic(@ShellOption({"--n", "--name"}) String topicName) {
        createDestination(topicName, DestinationType.TOPIC);
        return "Created";
    }

    @ShellMethod(key = "create-queue", value = "Creates queue in message broker")
    public String createQueue(@ShellOption({"--n", "--name"}) String queueName) {
        createDestination(queueName, DestinationType.QUEUE);
        return "Created";
    }

    private void createDestination(String destination, DestinationType type) {
        if (StringUtils.isNotBlank(destination)) {
            brokerControllerApi.registerDestination(MsgDestination.createDestination(destination, type));
        } else {
            throw new IllegalArgumentException("Argument should not be empty");
        }
    }
}
