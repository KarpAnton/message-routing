package com.andersenlab.messagebroker.controller;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.pubsub.Commit;
import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messagebroker.pubsub.Messages;
import com.andersenlab.messagebroker.service.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrokerController implements BrokerControllerApi {

    private static final Logger LOG = LoggerFactory.getLogger(BrokerController.class);

    @Autowired
    private BrokerService brokerService;

    @Override
    public void registerDestination(MsgDestination destination) {
        LOG.info("Incoming request to register new destination: {}", destination.getName());
        Long id = brokerService.registerDestination(destination);
        LOG.info("{} with id {} has been registered", destination.getName(), id);
    }

    @Override
    public void sendMessage(Message message) {
        LOG.info("Received message with correlationId {}", message.getCorrelationId());
        Message sentMessage = brokerService.sendMessage(message);
        LOG.info("Message {} has been saved", message.getCorrelationId());
    }

    @Override
    public Messages requestAvailableMessages(String consumerName, Integer batchSize) {
        LOG.info("Requesting {} messages for {}", batchSize, consumerName);
        return brokerService.requestAvailableMessages(consumerName, batchSize);
    }

    @Override
    public void commitMessages(Commit commit) {
        LOG.info("Committing message(-s). {} message(-s) for {} destination", commit.getAcks(), commit.getDestinationName());
        brokerService.commitMessages(commit);
    }
}
