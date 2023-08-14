package com.andersenlab.messagebroker.service;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.entity.Consumer;
import com.andersenlab.messagebroker.entity.Destination;
import com.andersenlab.messagebroker.entity.Offset;
import com.andersenlab.messagebroker.exception.DestinationNotFoundException;
import com.andersenlab.messagebroker.pubsub.Subscriber;
import com.andersenlab.messagebroker.repository.ConsumerRepository;
import com.andersenlab.messagebroker.repository.DestinationRepository;
import com.andersenlab.messagebroker.repository.OffsetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class SubscriberServiceTest extends BaseTest {

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private BrokerService brokerService;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private OffsetRepository offsetRepository;

    @Test
    public void shouldRegisterConsumer() {
        String subscriberName = name("subscriber-1");
        String destination = name("queue-queue-3");
        Subscriber subscriber = createSubscriber(subscriberName, destination);

        brokerService.registerDestination(MsgDestination.createDestination(destination, null));
        subscriberService.register(subscriber);

        Consumer consumer = consumerRepository.findByName(subscriberName);

        Assertions.assertNotNull(consumer);
        Assertions.assertEquals(subscriberName, consumer.getName());
        Assertions.assertEquals(subscriber.getDestination().getName(), consumer.getDestination().getName());
    }

    @Test
    public void shouldThrowDestinationNotFoundExceptionDuringSubscribing() {
        String subscriberName = name("subscriber-2");
        String destination = name("queue-test-queue-2");
        Subscriber subscriber = createSubscriber(subscriberName, destination);

        Assertions.assertThrows(DestinationNotFoundException.class, () -> subscriberService.register(subscriber));
    }

    @Test
    public void shouldUnregisterSubscriber() {
        String subscriberName = name("Consumer-2");
        String destinationName = name("queue-test-queue-3");
        Subscriber subscriber = createSubscriber(subscriberName, destinationName);

        brokerService.registerDestination(subscriber.getDestination());
        subscriberService.register(subscriber);

        Consumer foundConsumer = consumerRepository.findByName(subscriberName);
        Destination foundDestination = destinationRepository.findByName(subscriber.getDestination().getName());

        Assertions.assertNotNull(foundConsumer);
        Assertions.assertNotNull(foundDestination);

        Offset offset = foundConsumer.getOffset();
        Long offsetId = offset.getId();

        subscriberService.unregister(subscriberName);

        foundConsumer = consumerRepository.findByName(subscriberName);
        foundDestination = destinationRepository.findByName(subscriber.getDestination().getName());
        offset = offsetRepository.findById(offsetId).orElse(null);

        Assertions.assertNull(foundConsumer);
        Assertions.assertNotNull(foundDestination);
        Assertions.assertNull(offset);
    }

    @Test
    public void shouldDetachSubscriber() {
        String subscriberName = name("Consumer-3");
        String destinationName = name("queue-test-queue-4");
        Subscriber subscriber = createSubscriber(subscriberName, destinationName);

        brokerService.registerDestination(subscriber.getDestination());
        subscriberService.register(subscriber);

        Consumer foundConsumer = consumerRepository.findByName(subscriberName);
        Destination foundDestination = destinationRepository.findByName(subscriber.getDestination().getName());

        Assertions.assertNotNull(foundConsumer);
        Assertions.assertNotNull(foundConsumer.getOffset());
        Assertions.assertNotNull(foundDestination);
        Assertions.assertNotNull(foundConsumer.getDestination());

        Offset offset = foundConsumer.getOffset();
        Long offsetId = offset.getId();

        subscriberService.detach(subscriberName);

        foundConsumer = consumerRepository.findByName(subscriberName);
        foundDestination = destinationRepository.findByName(subscriber.getDestination().getName());
        offset = offsetRepository.findById(offsetId).orElse(null);

        Assertions.assertNotNull(foundConsumer);
        Assertions.assertNotNull(foundDestination);
        Assertions.assertNull(foundConsumer.getDestination());
        Assertions.assertNull(offset);
        Assertions.assertNull(foundConsumer.getOffset());
    }

    private Subscriber createSubscriber(String name, String destination) {
        Subscriber subscriber = new Subscriber();
        subscriber.setName(name);
        subscriber.setDestination(MsgDestination.createDestination(destination, null));
        subscriber.setCreatedAt(LocalDateTime.now());

        return subscriber;
    }
}
