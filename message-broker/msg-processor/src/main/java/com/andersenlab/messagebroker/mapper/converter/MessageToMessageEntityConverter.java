package com.andersenlab.messagebroker.mapper.converter;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.entity.Destination;
import com.andersenlab.messagebroker.entity.Producer;
import com.andersenlab.messagebroker.exception.DestinationNotFoundException;
import com.andersenlab.messagebroker.exception.ProducerNotFoundException;
import com.andersenlab.messagebroker.mapper.Mapper;
import com.andersenlab.messagebroker.mapper.annotation.Converter;
import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messagebroker.pubsub.Publisher;
import com.andersenlab.messagebroker.repository.DestinationRepository;
import com.andersenlab.messagebroker.repository.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Converter
public class MessageToMessageEntityConverter implements BaseConverter<Message, com.andersenlab.messagebroker.entity.Message> {

    @Autowired
    @Lazy
    private Mapper mapper;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Override
    public void convert(Message from, com.andersenlab.messagebroker.entity.Message to) {
        to.setPayload(from.getPayload());
        to.setSentAt(from.getSentAt());
        to.setCorrelationId(from.getCorrelationId());
        to.getHeaders().putAll(from.getHeaders());
        to.setDestination(mapDestination(from.getDestination()));
        to.setProducer(mapProducer(from.getPublisher()));
    }

    private Destination mapDestination(MsgDestination dest) {
        Destination destEntity = destinationRepository.findByName(dest.getName());
        if (destEntity != null) {
            return destEntity;
        } else {
            throw new DestinationNotFoundException(dest.getName());
        }
    }

    private Producer mapProducer(Publisher publisher) {
        Producer foundProducer = producerRepository.findByName(publisher.getName());
        if (foundProducer != null) {
            return foundProducer;
        } else {
            throw new ProducerNotFoundException(publisher.getName());
        }
    }
}
