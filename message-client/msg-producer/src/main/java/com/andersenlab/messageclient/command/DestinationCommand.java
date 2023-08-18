package com.andersenlab.messageclient.command;

import com.andersenlab.messageclient.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class DestinationCommand {

    @Autowired
    private DestinationService destinationService;

    @ShellMethod(key = "create-topic", value = "Creates topic in message broker")
    public String createTopic(@ShellOption({"--n", "--name"}) String topicName) {
        return destinationService.createTopic(topicName);
    }

    @ShellMethod(key = "create-queue", value = "Creates queue in message broker")
    public String createQueue(@ShellOption({"--n", "--name"}) String queueName) {
        return destinationService.createQueue(queueName);
    }
}
