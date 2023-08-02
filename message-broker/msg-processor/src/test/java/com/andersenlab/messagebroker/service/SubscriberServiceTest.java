package com.andersenlab.messagebroker.service;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.entity.Consumer;
import com.andersenlab.messagebroker.exception.DestinationNotFoundException;
import com.andersenlab.messagebroker.pubsub.Subscriber;
import com.andersenlab.messagebroker.repository.ConsumerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class SubscriberServiceTest {

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private BrokerService brokerService;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Test
    public void shouldRegisterConsumer() {
        String subscriberName = "subscriber-1";
        String destination = "queue-test-queue";
        Subscriber subscriber = createSubscriber(subscriberName, destination);

        brokerService.registerDestination(MsgDestination.createDestination(destination));
        subscriberService.register(subscriber);

        Consumer consumer = consumerRepository.findByName(subscriberName);

        Assertions.assertNotNull(consumer);
        Assertions.assertEquals(subscriberName, consumer.getName());
        Assertions.assertEquals(subscriber.getDestination().getName(), consumer.getDestination().getName());
    }

    @Test
    public void shouldThrowDestinationNotFoundExceptionDuringSubscribing() {
        String subscriberName = "subscriber-2";
        String destination = "queue-test-queue-2";
        Subscriber subscriber = createSubscriber(subscriberName, destination);

        Assertions.assertThrows(DestinationNotFoundException.class, () -> subscriberService.register(subscriber));
    }

    private Subscriber createSubscriber(String name, String destination) {
        Subscriber subscriber = new Subscriber();
        subscriber.setName(name);
        subscriber.setDestination(MsgDestination.createDestination(destination));
        subscriber.setCreatedAt(LocalDateTime.now());

        return subscriber;
    }
}
