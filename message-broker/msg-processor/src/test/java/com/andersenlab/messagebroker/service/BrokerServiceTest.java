package com.andersenlab.messagebroker.service;

import com.andersenlab.messagebroker.destination.DestinationType;
import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.entity.Destination;
import com.andersenlab.messagebroker.pubsub.*;
import com.andersenlab.messagebroker.repository.ConsumerRepository;
import com.andersenlab.messagebroker.repository.DestinationRepository;
import com.andersenlab.messagebroker.repository.MessageRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootTest
public class BrokerServiceTest extends BaseTest {

    @Autowired
    private BrokerService brokerService;
    @Autowired
    private PublisherService publisherService;
    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @ParameterizedTest
    @ValueSource(strings = {"queue-test-queue-0", "topic-test-topic-0"})
    public void shouldRegisterDestinationByPrefix(String destination) {
        MsgDestination msgDestination = MsgDestination.createDestination(name(destination), null);
        String destinationName = msgDestination.getName();
        brokerService.registerDestination(msgDestination);

        Destination foundDestination = destinationRepository.findByName(destinationName);

        Assertions.assertNotNull(foundDestination);
        Assertions.assertEquals(foundDestination.getName(), destinationName);
    }

    @ParameterizedTest
    @CsvSource(value = {"test-queue-1,QUEUE", "test-topic-1,TOPIC"})
    public void shouldRegisterDestinationByType(String destination, String type) {
        DestinationType destinationType = DestinationType.valueOf(type);
        MsgDestination msgDestination = MsgDestination.createDestination(name(destination), destinationType);
        String destinationName = msgDestination.getName();
        brokerService.registerDestination(msgDestination);

        Destination foundDestination = destinationRepository.findByName(destinationName);

        Assertions.assertNotNull(foundDestination);
        Assertions.assertEquals(foundDestination.getName(), destinationName);
    }

    @Test
    public void shouldSendMessageToBroker() {
        String testTopic = name("topic-test-topic-3");
        String payload = "Hello World!";

        Message message = createMessage(payload, MsgDestination.createDestination(testTopic, null));
        Publisher publisher = createPublisher(name("publisher-1"), message.getDestination());
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
    public void shouldReceiveMessagesFromQueueWithSingleConsumer() {
        String destinationName = name("test-queue-3");
        String basePayload = "Hello World!-";
        int batchSize = 3;
        int prevPosPointer, posPointer = 0;
        int amountOfAllMessages = 10;

        MsgDestination destination = MsgDestination.createDestination(destinationName, DestinationType.QUEUE);

        Publisher publisher = createPublisher(name("Publisher-1"), destination);
        publisherService.register(publisher);

        Subscriber subscriber = createSubscriber(name("Consumer-1"), destination);
        subscriberService.register(subscriber);

        List<Message> messages = generateMessages(publisher, destination, basePayload, amountOfAllMessages);

        List<Message> extractedMessages = brokerService.requestAvailableMessages(subscriber.getName(), batchSize).getMessages();
        brokerService.commitMessages(new Commit("", subscriber.getName(), extractedMessages));
        prevPosPointer = posPointer;
        posPointer = incrPosPointer(posPointer, batchSize, amountOfAllMessages);

        Assertions.assertFalse(extractedMessages.isEmpty());
        Assertions.assertEquals(batchSize, extractedMessages.size());
        Assertions.assertEquals(correlationIds(messages).subList(prevPosPointer, posPointer), correlationIds(extractedMessages));

        extractedMessages = brokerService.requestAvailableMessages(subscriber.getName(), batchSize).getMessages();
        brokerService.commitMessages(new Commit("", subscriber.getName(), extractedMessages));
        prevPosPointer = posPointer;
        posPointer = incrPosPointer(posPointer, batchSize, amountOfAllMessages);;

        Assertions.assertFalse(extractedMessages.isEmpty());
        Assertions.assertEquals(batchSize, extractedMessages.size());
        Assertions.assertEquals(correlationIds(messages).subList(prevPosPointer, posPointer), correlationIds(extractedMessages));

        batchSize = 5;
        extractedMessages = brokerService.requestAvailableMessages(subscriber.getName(), batchSize).getMessages();
        brokerService.commitMessages(new Commit("", subscriber.getName(), extractedMessages));
        prevPosPointer = posPointer;
        posPointer = incrPosPointer(posPointer, batchSize, amountOfAllMessages);

        Assertions.assertFalse(extractedMessages.isEmpty());
        Assertions.assertNotEquals(posPointer, extractedMessages.size());
        Assertions.assertEquals(correlationIds(messages).subList(prevPosPointer, posPointer), correlationIds(extractedMessages));

        extractedMessages = brokerService.requestAvailableMessages(subscriber.getName(), batchSize).getMessages();
        brokerService.commitMessages(new Commit("", subscriber.getName(), extractedMessages));
        prevPosPointer = posPointer;
        posPointer = incrPosPointer(posPointer, batchSize, amountOfAllMessages);

        Assertions.assertTrue(extractedMessages.isEmpty());
        Assertions.assertEquals(posPointer, amountOfAllMessages);
        Assertions.assertEquals(correlationIds(messages).subList(prevPosPointer, posPointer), correlationIds(extractedMessages));

        consumerRepository.deleteAll();
    }

    @Test
    public void shouldReceiveMessagesFromQueueWithMultipleConsumers() {
        String destinationName = name("test-queue-4");
        String basePayload = "Hello World!-";
        int amountOfAllMessages = 15;

        MsgDestination destination = MsgDestination.createDestination(destinationName, DestinationType.QUEUE);

        Publisher publisher = createPublisher(name("Publisher-2"), destination);
        publisherService.register(publisher);

        List<Subscriber> subscribers = createAndRegisterSubscribers(destination, 3);

        List<Message> messages = generateMessages(publisher, destination, basePayload, amountOfAllMessages);

        List<Message> extractedMessagesByConsumer1 = new ArrayList<>();
        List<Message> extractedMessagesByConsumer2 = new ArrayList<>();
        List<Message> extractedMessagesByConsumer3 = new ArrayList<>();

        Runnable runForConsumer1 = consumerJob(subscribers.get(0), 1, extractedMessagesByConsumer1);
        Runnable runForConsumer2 = consumerJob(subscribers.get(1), 2, extractedMessagesByConsumer2);
        Runnable runForConsumer3 = consumerJob(subscribers.get(2), 2, extractedMessagesByConsumer3);

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(runForConsumer1);
        executorService.submit(runForConsumer2);
        executorService.submit(runForConsumer3);
        shutdownAndAwaitTermination(executorService);

        int fullAmountOfProcessedMessages = extractedMessagesByConsumer1.size() + extractedMessagesByConsumer2.size() + extractedMessagesByConsumer3.size();

        Assertions.assertEquals(messages.size(), fullAmountOfProcessedMessages);
        Assertions.assertFalse(CollectionUtils.containsAny(extractedMessagesByConsumer1, extractedMessagesByConsumer2));
        Assertions.assertFalse(CollectionUtils.containsAny(extractedMessagesByConsumer1, extractedMessagesByConsumer3));
        Assertions.assertFalse(CollectionUtils.containsAny(extractedMessagesByConsumer2, extractedMessagesByConsumer3));

        consumerRepository.deleteAll();
    }

    @Test
    public void shouldReceiveMessagesFromTopicWithSingleConsumer() {
        String destinationName = name("test-topic-4");
        String basePayload = "Hello World!-";
        int amountOfAllMessages = 15;

        MsgDestination destination = MsgDestination.createDestination(destinationName, DestinationType.TOPIC);

        Publisher publisher = createPublisher(name("Publisher-3"), destination);
        publisherService.register(publisher);

        List<Subscriber> subscribers = createAndRegisterSubscribers(destination, 3);

        List<Message> messages = generateMessages(publisher, destination, basePayload, amountOfAllMessages);

        List<Message> extractedMessagesByConsumer1 = new ArrayList<>();
        List<Message> extractedMessagesByConsumer2 = new ArrayList<>();
        List<Message> extractedMessagesByConsumer3 = new ArrayList<>();

        Runnable runForConsumer1 = consumerJob(subscribers.get(0), 1, extractedMessagesByConsumer1);
        Runnable runForConsumer2 = consumerJob(subscribers.get(1), 2, extractedMessagesByConsumer2);
        Runnable runForConsumer3 = consumerJob(subscribers.get(2), 2, extractedMessagesByConsumer3);

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(runForConsumer1);
        executorService.submit(runForConsumer2);
        executorService.submit(runForConsumer3);
        shutdownAndAwaitTermination(executorService);

        Assertions.assertEquals(messages.size(), extractedMessagesByConsumer1.size());
        Assertions.assertEquals(messages.size(), extractedMessagesByConsumer2.size());
        Assertions.assertEquals(messages.size(), extractedMessagesByConsumer3.size());

        consumerRepository.deleteAll();
    }

    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(5, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ex) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private Runnable consumerJob(Subscriber subscriber, Integer batchSize, List<Message> container) {
        return () -> {
            boolean isRun = true;
            while (isRun) {
                Messages msgs = brokerService.requestAvailableMessages(subscriber.getName(), batchSize);
                if (msgs.isNotEmpty()) {
                    container.addAll(msgs.getMessages());
                    brokerService.commitMessages(new Commit(subscriber.getDestination().getName(), subscriber.getName(), msgs.getMessages()));
                } else {
                    isRun = false;
                }
            }
        };
    }

    private List<Message> generateMessages(Publisher publisher,
                                           MsgDestination destination,
                                           String basePayload,
                                           int amountOfAllMessages) {

        List<Message> messages = new ArrayList<>(amountOfAllMessages);
        for (int i = 0; i < amountOfAllMessages; i++) {
            Message message = createMessage(basePayload + i, destination);
            message.setPublisher(publisher);
            messages.add(message);
            brokerService.sendMessage(message);
        }
        return messages;
    }

    private List<String> correlationIds(List<Message> messages) {
        return messages.stream()
                .map(Message::getCorrelationId)
                .collect(Collectors.toList());
    }

    private int incrPosPointer(int prevPosPointer, int batchSize, int amountOfMessages) {
        int res = prevPosPointer + batchSize;
        return Math.min(res, amountOfMessages);
    }

    private Publisher createPublisher(String name, MsgDestination destination) {
        Publisher publisher = new Publisher();
        publisher.setName(name);
        publisher.setCreatedAt(LocalDateTime.now());
        publisher.setDestination(destination);
        return publisher;
    }

    private List<Subscriber> createAndRegisterSubscribers(MsgDestination destination, int amountOfSubscribers) {
        List<Subscriber> subscribers = new ArrayList<>();
        for (int i = 0; i< amountOfSubscribers; i++) {
            Subscriber subscriber = createSubscriber("Consumer-" + 1 + i, destination);
            subscriberService.register(subscriber);
            subscribers.add(subscriber);
        }
        return subscribers;
    }

    private Subscriber createSubscriber(String name, MsgDestination destination) {
        Subscriber subscriber = new Subscriber();
        subscriber.setName(name);
        subscriber.setDestination(destination);
        subscriber.setCreatedAt(LocalDateTime.now());
        return subscriber;
    }

    private Message createMessage(String payload, MsgDestination msgDestination) {
        Message message = new Message();
        message.setSentAt(LocalDateTime.now());
        message.setCorrelationId(UUID.randomUUID().toString());
        message.setPayload(payload);
        message.setDestination(msgDestination);
        return message;
    }
}
