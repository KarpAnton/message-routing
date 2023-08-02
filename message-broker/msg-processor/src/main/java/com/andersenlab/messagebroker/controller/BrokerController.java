package com.andersenlab.messagebroker.controller;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messagebroker.service.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BrokerController implements BrokerControllerApi {

    private static final Logger LOG = LoggerFactory.getLogger(BrokerController.class);

    @Autowired
    private BrokerService brokerService;

    @Override
    public void registerDestination(String destinationName) {
        LOG.info("Incoming request to register new destination: {}", destinationName);
        MsgDestination destination = MsgDestination.createDestination(destinationName);
        Long id = brokerService.registerDestination(destination);
        LOG.info("{} with id {} has been registered", destination.getName(), id);
    }

    @Override
    public void sendMessage(Message message) {
        LOG.info("Received message with correlationId {}", message.getCorrelationId());
        brokerService.sendMessage(message);
        LOG.info("Message {} has been saved", message.getCorrelationId());
    }

    @Override
    public List<Message> requestAvailableMessages(String consumerName, String destinationName, Integer batchSize) {
        return null;
    }

    @Override
    public void commitMessage(String correlationId, String consumerName) {

    }
}
