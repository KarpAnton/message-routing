package com.andersenlab.messagebroker.service;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.entity.Destination;
import com.andersenlab.messagebroker.entity.Producer;
import com.andersenlab.messagebroker.exception.ProducerNotFoundException;
import com.andersenlab.messagebroker.pubsub.Publisher;
import com.andersenlab.messagebroker.repository.DestinationRepository;
import com.andersenlab.messagebroker.repository.ProducerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
public class PublisherServiceTest extends BaseTest {

    @Autowired
    private PublisherService publisherService;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Test
    @Transactional
    public void shouldRegisterPublisher() {
        String publisherName = name("test-publisher-1");
        String destination = name("topic-test-topic-1");
        Publisher publisher = createPublisher(publisherName, destination);
        publisherService.register(publisher);

        Producer producer = producerRepository.findByName(publisherName);

        Assertions.assertNotNull(producer);
        Assertions.assertEquals(publisherName, producer.getName());
        Assertions.assertEquals(publisher.getDestination().getName(), producer.getDestination().getName());
    }

    @Test
    public void shouldUnregisterPublisher() {
        String publisherName = name("test-publisher-2");
        Publisher publisher = createPublisher(publisherName, name("topic-test-topic-2"));
        publisherService.register(publisher);

        Producer producer = producerRepository.findByName(publisherName);

        Assertions.assertNotNull(producer);
        Assertions.assertEquals(publisherName, producer.getName());

        publisherService.unregister(publisherName);

        Destination foundDestination = destinationRepository.findByName(publisher.getDestination().getName());

        Assertions.assertFalse(producerRepository.existsById(producer.getId()));
        Assertions.assertNotNull(foundDestination);
    }

    @Test
    public void shouldThrowProducerNotFoundException() {
        Assertions.assertThrows(ProducerNotFoundException.class, () -> publisherService.unregister(name("publisher-3")));
    }

    private Publisher createPublisher(String name, String destination) {
        Publisher publisher = new Publisher();
        publisher.setName(name);
        publisher.setCreatedAt(LocalDateTime.now());
        publisher.setDestination(MsgDestination.createDestination(destination, null));
        return publisher;
    }
}
