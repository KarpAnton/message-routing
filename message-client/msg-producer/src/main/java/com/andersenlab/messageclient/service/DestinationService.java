package com.andersenlab.messageclient.service;

import com.andersenlab.client.service.BaseService;
import com.andersenlab.messagebroker.controller.BrokerControllerApi;
import com.andersenlab.messagebroker.destination.DestinationType;
import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.pubsub.Publisher;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Service;

@Service
public class DestinationService extends BaseService<Publisher> {

    @Autowired
    private BrokerControllerApi brokerControllerApi;

    public String createTopic(@ShellOption({"--n", "--name"}) String topicName) {
        createDestination(topicName, DestinationType.TOPIC);
        return "Created";
    }

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
