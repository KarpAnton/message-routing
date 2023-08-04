package com.andersenlab.messagebroker.service;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.destination.MsgQueue;
import com.andersenlab.messagebroker.entity.*;
import com.andersenlab.messagebroker.mapper.Mapper;
import com.andersenlab.messagebroker.repository.ConsumerRepository;
import com.andersenlab.messagebroker.repository.DestinationRepository;
import com.andersenlab.messagebroker.repository.MessageRepository;
import com.andersenlab.messagebroker.repository.pageable.OffsetLimitRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        Destination destEntity = dest instanceof MsgQueue
                ? mapper.map(dest, Queue.class)
                : mapper.map(dest, Topic.class);
        destEntity = destinationRepository.save(destEntity);
        return destEntity.getId();
    }

    @Transactional
    public com.andersenlab.messagebroker.pubsub.Message sendMessage(com.andersenlab.messagebroker.pubsub.Message message) {
        Message messageEntity = mapper.map(message, Message.class);
        messageEntity.setCreatedAt(LocalDateTime.now());
        messageEntity = messageRepository.save(messageEntity);
        return mapper.map(messageEntity, com.andersenlab.messagebroker.pubsub.Message.class);
    }

    @Transactional
    public List<com.andersenlab.messagebroker.pubsub.Message> requestAvailableMessages(String consumerName,
                                                                                       Integer batchSize) {

        Consumer foundConsumer = consumerRepository.findByName(consumerName);
        Offset offset = foundConsumer.getOffset();
        int posPointer = offset.getPosPointer();
        OffsetLimitRequest offsetLimitRequest = new OffsetLimitRequest(posPointer, batchSize, Sort.by("createdAt").ascending());
        List<Message> messages = messageRepository.findAll(offsetLimitRequest).getContent();
        offset.setPosPointer(posPointer + messages.size());

        return messages.stream()
                .map(message -> mapper.map(message, com.andersenlab.messagebroker.pubsub.Message.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void commitMessage(String msgCorrelationId, String consumerName) {
        Consumer consumer = consumerRepository.findByName(consumerName);

    }

}
