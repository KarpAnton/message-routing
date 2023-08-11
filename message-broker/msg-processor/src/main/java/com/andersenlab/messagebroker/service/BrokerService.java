package com.andersenlab.messagebroker.service;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.destination.MsgQueue;
import com.andersenlab.messagebroker.entity.*;
import com.andersenlab.messagebroker.exception.ConsumerNotFoundException;
import com.andersenlab.messagebroker.exception.ProducerAlreadyExistsException;
import com.andersenlab.messagebroker.mapper.Mapper;
import com.andersenlab.messagebroker.pubsub.Commit;
import com.andersenlab.messagebroker.pubsub.Messages;
import com.andersenlab.messagebroker.repository.ConsumerRepository;
import com.andersenlab.messagebroker.repository.DestinationRepository;
import com.andersenlab.messagebroker.repository.MessageRepository;
import com.andersenlab.messagebroker.repository.request.OffsetLimitRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrokerService {

    @Autowired
    private DestinationRepository destinationRepository;
    @Autowired
    private ConsumerRepository consumerRepository;
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private Mapper mapper;

    @Transactional
    public Long registerDestination(MsgDestination dest) {
        Destination destination = destinationRepository.findByName(dest.getName());
        if (destination == null) {
            destination = dest instanceof MsgQueue
                    ? mapper.map(dest, Queue.class)
                    : mapper.map(dest, Topic.class);
            destination = destinationRepository.save(destination);
            return destination.getId();
        } else {
            throw new ProducerAlreadyExistsException("Producer with name " + dest.getName() + " already exists");
        }
    }

    @Transactional
    public com.andersenlab.messagebroker.pubsub.Message sendMessage(com.andersenlab.messagebroker.pubsub.Message message) {
        Message messageEntity = mapper.map(message, Message.class);
        messageEntity.setCreatedAt(LocalDateTime.now());
        messageEntity = messageRepository.save(messageEntity);
        return mapper.map(messageEntity, com.andersenlab.messagebroker.pubsub.Message.class);
    }

    @Transactional
    public Messages requestAvailableMessages(String consumerName,
                                             Integer batchSize) {

        Consumer foundConsumer = consumerRepository.findByName(consumerName);
        if (foundConsumer == null) {
            throw new ConsumerNotFoundException("Consumer not found: " + consumerName);
        }
        Destination destination = foundConsumer.getDestination();
        List<Message> messages;
        if (destination instanceof Queue queue) {
            messages = extractMessagesFromQueue(queue, batchSize);
        } else if (destination instanceof Topic topic) {
            messages = extractMessagesFromTopic(foundConsumer, topic, batchSize);
        } else {
            messages = Collections.emptyList();
        }

        messages.forEach(message -> message.setSent(true));

        return new Messages(
                messages.stream()
                        .map(message -> mapper.map(message, com.andersenlab.messagebroker.pubsub.Message.class))
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void commitMessages(Commit commit) {
        Consumer consumer = consumerRepository.findByName(commit.getConsumerName());
        if (consumer == null) {
            throw new ConsumerNotFoundException("Consumer not found: " + commit.getConsumerName());
        }

        Destination destination = consumer.getDestination();
        if (destination instanceof Queue) {
            // removing read message from the queue
            messageRepository.deleteByCorrelationIdIn(commit.getMessageCorrelationIds());
        } else {
            // increasing offset in case of topic
            Offset offset = consumer.getOffset();
            offset.setPosPointer(offset.getPosPointer() + commit.getAcks());
        }
    }

    private List<Message> extractMessagesFromQueue(Queue queue, Integer batchSize) {
        OffsetLimitRequest offsetLimitRequest = new OffsetLimitRequest(0, batchSize, Sort.by("createdAt").ascending());
        return messageRepository.findAllByDestinationAndIsSentFalse(queue.getId(), offsetLimitRequest);
    }

    private List<Message> extractMessagesFromTopic(Consumer consumer, Topic topic, Integer batchSize) {
        Offset offset = consumer.getOffset();
        int posPointer = offset.getPosPointer();
        OffsetLimitRequest offsetLimitRequest = new OffsetLimitRequest(posPointer, batchSize, Sort.by("createdAt").ascending());
        return messageRepository.findAllByDestination(topic, offsetLimitRequest);
    }
}
