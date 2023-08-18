package com.andersenlab.messagebroker.service;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.destination.MsgQueue;
import com.andersenlab.messagebroker.entity.*;
import com.andersenlab.messagebroker.exception.ConsumerNotFoundException;
import com.andersenlab.messagebroker.exception.ProducerAlreadyExistsException;
import com.andersenlab.messagebroker.pubsub.Commit;
import com.andersenlab.messagebroker.pubsub.Messages;
import com.andersenlab.messagebroker.repository.ConsumerRepository;
import com.andersenlab.messagebroker.repository.DestinationRepository;
import com.andersenlab.messagebroker.repository.MessageRepository;
import com.andersenlab.messagebroker.repository.request.OffsetLimitRequest;
import org.akhome.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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
            throw new ProducerAlreadyExistsException(dest.getName());
        }
    }

    @Transactional
    public com.andersenlab.messagebroker.pubsub.Message sendMessage(com.andersenlab.messagebroker.pubsub.Message message) {
        Message messageEntity = mapper.map(message, Message.class);
        messageEntity.setCreatedAt(LocalDateTime.now());
        messageEntity = messageRepository.save(messageEntity);
        return mapper.map(messageEntity, com.andersenlab.messagebroker.pubsub.Message.class);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Messages requestAvailableMessages(String consumerName,
                                             Integer batchSize) {

        Consumer foundConsumer = consumerRepository.findByName(consumerName);
        if (foundConsumer == null) {
            throw new ConsumerNotFoundException(consumerName);
        }
        Destination destination = foundConsumer.getDestination();

        List<Message> messages = extractMessages(foundConsumer, destination, batchSize);

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
            throw new ConsumerNotFoundException(commit.getConsumerName());
        }

        Destination destination = consumer.getDestination();
        if (destination instanceof Queue) {
            // removing read message from the queue
            messageRepository.deleteByIdIn(commit.getMessageIds());
        } else {
            // increasing offset in case of topic
            Offset offset = consumer.getOffset();
            offset.setPosPointer(offset.getPosPointer() + commit.getAcks());
        }
    }

    private List<Message> extractMessages(Consumer consumer, Destination destination, Integer batchSize) {
        List<Message> messages;
        if (destination instanceof Queue queue) {
            messages = extractMessages(consumer, queue, batchSize);
            messages.forEach(message -> message.setRequestedBy(consumer));
        } else if (destination instanceof Topic topic) {
            messages = extractMessages(consumer, topic, batchSize);
        } else {
            messages = Collections.emptyList();
        }
        return messages;
    }

    private List<Message> extractMessages(Consumer consumer, Queue queue, Integer batchSize) {
        OffsetLimitRequest offsetLimitRequest = new OffsetLimitRequest(0, batchSize, Sort.by("createdAt").ascending());
        return messageRepository.findAllByDestination(queue.getId(), consumer.getId(), offsetLimitRequest);
    }

    private List<Message> extractMessages(Consumer consumer, Topic topic, Integer batchSize) {
        Offset offset = consumer.getOffset();
        int posPointer = offset.getPosPointer();
        OffsetLimitRequest offsetLimitRequest = new OffsetLimitRequest(posPointer, batchSize, Sort.by("createdAt").ascending());
        return messageRepository.findAllByDestination(topic, offsetLimitRequest);
    }
}
