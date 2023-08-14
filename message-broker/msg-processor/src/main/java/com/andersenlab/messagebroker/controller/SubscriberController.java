package com.andersenlab.messagebroker.controller;

import com.andersenlab.messagebroker.entity.Consumer;
import com.andersenlab.messagebroker.pubsub.Subscriber;
import com.andersenlab.messagebroker.service.SubscriberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriberController implements SubscriberControllerApi {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriberController.class);

    @Autowired
    private SubscriberService subscriberService;

    @Override
    public ResponseEntity<Void> subscribe(Subscriber subscriber) {
        LOG.info("Incoming request to register subscriber: {}", subscriber.getName());
        Consumer consumer = subscriberService.register(subscriber);
        LOG.info("Created consumer: {}", consumer.getName());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> unsubscribe(String subscriberName) {
        LOG.info("Incoming request to unregister subscriber: {}", subscriberName);
        subscriberService.unregister(subscriberName);
        LOG.info("Consumer {} has been removed successfully", subscriberName );
        return ResponseEntity.ok().build();
    }

    @Override
    public void detach(String subscriberName) {
        LOG.info("Detaching {} subscriber from destination (topic or queue)", subscriberName);
    }
}
