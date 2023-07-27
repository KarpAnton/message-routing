package com.andersenlab.messagebroker.service;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.destination.MsgQueue;
import com.andersenlab.messagebroker.entity.*;
import com.andersenlab.messagebroker.exception.ConsumerAlreadyExistsException;
import com.andersenlab.messagebroker.mapper.Mapper;
import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messagebroker.pubsub.Publisher;
import com.andersenlab.messagebroker.pubsub.Subscriber;
import com.andersenlab.messagebroker.repository.ConsumerRepository;
import com.andersenlab.messagebroker.repository.DestinationRepository;
import com.andersenlab.messagebroker.repository.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BrokerService {

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    private ProducerRepository producerRepository;

    @Autowired
    private Mapper mapper;

    @Transactional
    public Long registerDestination(MsgDestination dest) {
        Destination destEntity = dest instanceof MsgQueue ? mapper.map(dest, Queue.class) : mapper.map(dest, Topic.class);
        destEntity = destinationRepository.save(destEntity);
        return destEntity.getId();
    }

    public void sendMessage(Message message) {

    }

}
