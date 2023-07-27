package com.andersenlab.messageclient.command;

import com.andersenlab.messagebroker.controller.BrokerControllerApi;
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
    public String createTopic(@ShellOption({"-n", "-name"}) String topicName) {
        createDestination(TOPIC_PREFIX + topicName);
        return "Created";
    }

    @ShellMethod(key = "create-queue", value = "Creates queue in message broker")
    public String createQueue(@ShellOption({"-n", "-name"}) String queueName) {
        createDestination(QUEUE_PREFIX + queueName);
        return "Created";
    }

    private void createDestination(String destination) {
        if (StringUtils.isNotBlank(destination)) {
            brokerControllerApi.registerDestination(destination);
        } else {
            throw new IllegalArgumentException("Argument should not be empty");
        }
    }
}
