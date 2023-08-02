package com.andersenlab.messagebroker.service;

import com.andersenlab.messagebroker.entity.Producer;
import com.andersenlab.messagebroker.mapper.Mapper;
import com.andersenlab.messagebroker.mapper.exception.ProducerNotFoundException;
import com.andersenlab.messagebroker.pubsub.Publisher;
import com.andersenlab.messagebroker.repository.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublisherService {

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private Mapper mapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public void register(Publisher publisher) {
        Producer foundProducer = producerRepository.findByName(publisher.getName());
        if (foundProducer != null) {
            mapper.map(publisher, foundProducer);
        } else {
            Producer producer = mapper.map(publisher, Producer.class);
            producerRepository.save(producer);
        }
    }

    @Transactional
    public void unregister(Publisher publisher) {
        unregister(publisher.getName());
    }

    @Transactional
    public void unregister(String publisherName) {
        Producer producer = producerRepository.findByName(publisherName);
        if (producer != null) {
            producerRepository.delete(producer);
        } else {
            throw new ProducerNotFoundException("Producer " + publisherName + " not found");
        }
    }
}
