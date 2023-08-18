package com.andersenlab.messagebroker.converter;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.entity.Destination;
import com.andersenlab.messagebroker.entity.Producer;
import com.andersenlab.messagebroker.exception.DestinationNotFoundException;
import com.andersenlab.messagebroker.exception.ProducerNotFoundException;
import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messagebroker.pubsub.Publisher;
import com.andersenlab.messagebroker.repository.DestinationRepository;
import com.andersenlab.messagebroker.repository.ProducerRepository;
import org.akhome.mapper.Mapper;
import org.akhome.mapper.annotation.Converter;
import org.akhome.mapper.converter.BaseConverter;
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
        to.setId(from.getId());
        to.setPayload(from.getPayload());
        to.setSentAt(from.getSentAt());
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
