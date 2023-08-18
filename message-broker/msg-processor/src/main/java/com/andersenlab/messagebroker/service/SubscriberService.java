package com.andersenlab.messagebroker.service;

import com.andersenlab.messagebroker.entity.Consumer;
import com.andersenlab.messagebroker.exception.ConsumerNotFoundException;
import com.andersenlab.messagebroker.pubsub.Subscriber;
import com.andersenlab.messagebroker.repository.ConsumerRepository;
import org.akhome.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscriberService {

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private Mapper mapper;

    @Transactional
    public Consumer register(Subscriber subscriber) {
        Consumer foundConsumer = consumerRepository.findByName(subscriber.getName());
        if (foundConsumer != null) {
            return mapper.map(subscriber, foundConsumer);
        } else {
            Consumer consumer = mapper.map(subscriber, Consumer.class);
            return consumerRepository.save(consumer);
        }
    }

    @Transactional
    public void unregister(String subscriberName) {
        Consumer foundConsumer = consumerRepository.findByName(subscriberName);
        if (foundConsumer != null) {
            consumerRepository.delete(foundConsumer);
        } else {
            throw new ConsumerNotFoundException(subscriberName);
        }
    }

    @Transactional
    public void detach(String subscriberName) {
        Consumer foundConsumer = consumerRepository.findByName(subscriberName);
        if (foundConsumer != null) {
            foundConsumer.setDestination(null);
            foundConsumer.setOffset(null);
        } else {
            throw new ConsumerNotFoundException(subscriberName);
        }
    }
}
