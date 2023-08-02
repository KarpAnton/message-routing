package com.andersenlab.messagebroker.service;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.destination.MsgQueue;
import com.andersenlab.messagebroker.entity.*;
import com.andersenlab.messagebroker.mapper.Mapper;
import com.andersenlab.messagebroker.repository.ConsumerRepository;
import com.andersenlab.messagebroker.repository.DestinationRepository;
import com.andersenlab.messagebroker.repository.MessageRepository;
import com.andersenlab.messagebroker.repository.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public void sendMessage(com.andersenlab.messagebroker.pubsub.Message message) {
        Message messageEntity = mapper.map(message, Message.class);
        messageRepository.save(messageEntity);
    }

    @Transactional
    public List<com.andersenlab.messagebroker.pubsub.Message> requestAvailableMessages(String consumerName,
                                                                                       String destinationName,
                                                                                       Integer batchSize) {
        return null;
    }

    @Transactional
    public void commitMessage(String msgCorrelationId, String consumerName) {
        Consumer consumer = consumerRepository.findByName(consumerName);

    }

}
