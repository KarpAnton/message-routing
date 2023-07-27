package com.andersenlab.messagebroker.controller;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.entity.Consumer;
import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messagebroker.pubsub.Publisher;
import com.andersenlab.messagebroker.pubsub.Subscriber;
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
    public void registerDestination(String destinationName) {
        LOG.info("Incoming request to register new destination: " + destinationName);
        MsgDestination destination = MsgDestination.createDestination(destinationName);
        Long id = brokerService.registerDestination(destination);
        LOG.info(destination.getName() + "with id " + id + " has been registered");
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        LOG.info("Incoming request to register subscriber: " + subscriber.getName());
        Consumer consumer = brokerService.subscribe(subscriber);
        LOG.info("Created consumer: " + consumer.getName());
    }

    @Override
    public void sendMessage(Message message) {

    }

    @Override
    public void registerProducer(Publisher publisher) {

    }
}
