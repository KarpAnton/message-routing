package com.andersenlab.messagebroker.controller;

import com.andersenlab.messagebroker.entity.Consumer;
import com.andersenlab.messagebroker.pubsub.Subscriber;
import com.andersenlab.messagebroker.service.SubscriberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriberController implements SubscriberControllerApi {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriberController.class);

    @Autowired
    private SubscriberService subscriberService;

    @Override
    public void subscribe(Subscriber subscriber) {
        LOG.info("Incoming request to register subscriber: {}", subscriber.getName());
        Consumer consumer = subscriberService.subscribe(subscriber);
        LOG.info("Created consumer: {}", consumer.getName());
    }

    @Override
    public void unsubscribe(String subscriberName) {

    }
}