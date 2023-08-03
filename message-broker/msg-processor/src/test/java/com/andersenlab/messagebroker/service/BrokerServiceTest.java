package com.andersenlab.messagebroker.service;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.entity.Consumer;
import com.andersenlab.messagebroker.entity.Destination;
import com.andersenlab.messagebroker.entity.Offset;
import com.andersenlab.messagebroker.entity.Queue;
import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messagebroker.pubsub.Publisher;
import com.andersenlab.messagebroker.repository.ConsumerRepository;
import com.andersenlab.messagebroker.repository.DestinationRepository;
import com.andersenlab.messagebroker.repository.MessageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class BrokerServiceTest {

    @Autowired
    private BrokerService brokerService;
    @Autowired
    private PublisherService publisherService;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @ParameterizedTest
    @ValueSource(strings = {"queue-test-queue", "topic-test-topic"})
    public void shouldRegisterDestination(String destination) {
        MsgDestination msgDestination = MsgDestination.createDestination(destination);
        String destinationName = msgDestination.getName();
        brokerService.registerDestination(msgDestination);

        Destination foundDestination = destinationRepository.findByName(destinationName);

        Assertions.assertNotNull(foundDestination);
        Assertions.assertEquals(foundDestination.getName(), destinationName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"test-queue", "test-topic"})
    public void shouldThrowIllegalArgumentException(String destination) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> MsgDestination.createDestination(destination));
    }

    @Test
    public void shouldSendMessageToBroker() {
        String testTopic = "topic-test-topic-2";
        String payload = "Hello World!";

        Message message = createMessage(payload, testTopic);
        Publisher publisher = createPublisher("publisher-1", message.getDestination());
        message.setPublisher(publisher);

        publisherService.register(publisher);
        Message sentMessage = brokerService.sendMessage(message);

        Assertions.assertNotNull(sentMessage);
        Assertions.assertEquals(message.getDestination().getName(), sentMessage.getDestination().getName());
        Assertions.assertEquals(publisher.getName(), sentMessage.getPublisher().getName());
        Assertions.assertEquals(payload, message.getPayload());

        Optional<com.andersenlab.messagebroker.entity.Message> sentMessageEntity = messageRepository.findById(sentMessage.getId());

        Assertions.assertNotNull(sentMessageEntity.get());
        Assertions.assertEquals(payload, sentMessageEntity.get().getPayload());
        Assertions.assertEquals(publisher.getName(), sentMessageEntity.get().getProducer().getName());
    }

    @Test
    @Transactional
    public void shouldReceiveMessages() {
        String destination = "queue-test-queue-3";
        String basePayload = "Hello World!-";
        int batchSize = 3;
        int posPointer = 0;
        int amountOfMessages = 10;

        Consumer consumer = new Consumer();
        consumer.setDestination(new Queue("test-queue-3"));
        consumer.setOffset(new Offset());
        consumer.setName("Consumer-1");

        consumerRepository.save(consumer);

        Publisher publisher = createPublisher("Publisher-1", MsgDestination.createDestination(destination));
        publisherService.register(publisher);

        for (int i = 0; i <= amountOfMessages; i++) {
            Message message = createMessage(basePayload + i, destination);
            message.setPublisher(publisher);
            brokerService.sendMessage(message);
        }

        List<Message> extractedMessages = brokerService.requestAvailableMessages(consumer.getName(), batchSize);
        consumer = consumerRepository.findByName(consumer.getName());
        posPointer += batchSize;

        Assertions.assertFalse(extractedMessages.isEmpty());
        Assertions.assertEquals(batchSize, extractedMessages.size());
        Assertions.assertEquals(posPointer, consumer.getOffset().getPosPointer());

        extractedMessages = brokerService.requestAvailableMessages(consumer.getName(), batchSize);
        posPointer += batchSize;

        Assertions.assertFalse(extractedMessages.isEmpty());
        Assertions.assertEquals(batchSize, extractedMessages.size());
        Assertions.assertEquals(posPointer, consumer.getOffset().getPosPointer());

        batchSize = 5;
        extractedMessages = brokerService.requestAvailableMessages(consumer.getName(), batchSize);

        Assertions.assertFalse(extractedMessages.isEmpty());
        Assertions.assertNotEquals(amountOfMessages, extractedMessages.size());
    }

    private Publisher createPublisher(String name, MsgDestination destination) {
        Publisher publisher = new Publisher();
        publisher.setName(name);
        publisher.setCreatedAt(LocalDateTime.now());
        publisher.setDestination(destination);
        return publisher;
    }

    private Message createMessage(String payload, String destination) {
        Message message = new Message();
        message.setSentAt(LocalDateTime.now());
        message.setCorrelationId(UUID.randomUUID().toString());
        message.setPayload(payload);
        MsgDestination msgDestination = MsgDestination.createDestination(destination);
        message.setDestination(msgDestination);
        return message;
    }
}
