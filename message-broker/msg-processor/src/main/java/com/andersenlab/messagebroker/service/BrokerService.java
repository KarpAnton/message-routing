package com.andersenlab.messagebroker.service;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.destination.MsgQueue;
import com.andersenlab.messagebroker.entity.Consumer;
import com.andersenlab.messagebroker.entity.Destination;
import com.andersenlab.messagebroker.entity.Queue;
import com.andersenlab.messagebroker.entity.Topic;
import com.andersenlab.messagebroker.exception.ConsumerAlreadyExistsException;
import com.andersenlab.messagebroker.mapper.Mapper;
import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messagebroker.pubsub.Publisher;
import com.andersenlab.messagebroker.pubsub.Subscriber;
import com.andersenlab.messagebroker.repository.ConsumerRepository;
import com.andersenlab.messagebroker.repository.DestinationRepository;
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

    @Autowired
    private Mapper mapper;

    @Transactional
    public Long registerDestination(MsgDestination dest) {
        Destination destEntity = dest instanceof MsgQueue ? mapper.map(dest, Queue.class) : mapper.map(dest, Topic.class);
        destEntity = destinationRepository.save(destEntity);
        return destEntity.getId();
    }

    @Transactional
    public Consumer subscribe(Subscriber subscriber) {
        Consumer consumer = mapper.map(subscriber, Consumer.class);
        Consumer foundConsumer = consumerRepository.findByName(consumer.getName());
        if (foundConsumer == null) {
            consumer.setCreatedAt(LocalDateTime.now());
            return consumerRepository.save(consumer);
        } else {
            throw new ConsumerAlreadyExistsException("Consumer " + foundConsumer.getName() + " already exists");
        }
    }

    public void sendMessage(Message message) {

    }

    public void registerProducer(Publisher publisher) {

    }

}
