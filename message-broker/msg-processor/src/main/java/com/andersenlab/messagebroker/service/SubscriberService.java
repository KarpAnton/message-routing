package com.andersenlab.messagebroker.service;

import com.andersenlab.messagebroker.entity.Consumer;
import com.andersenlab.messagebroker.exception.ConsumerAlreadyExistsException;
import com.andersenlab.messagebroker.mapper.Mapper;
import com.andersenlab.messagebroker.pubsub.Subscriber;
import com.andersenlab.messagebroker.repository.ConsumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SubscriberService {

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private Mapper mapper;

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
}
