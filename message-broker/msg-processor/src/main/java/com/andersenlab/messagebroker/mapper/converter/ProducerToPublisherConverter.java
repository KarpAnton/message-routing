package com.andersenlab.messagebroker.mapper.converter;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.destination.MsgQueue;
import com.andersenlab.messagebroker.destination.MsgTopic;
import com.andersenlab.messagebroker.entity.Destination;
import com.andersenlab.messagebroker.entity.Producer;
import com.andersenlab.messagebroker.entity.Queue;
import com.andersenlab.messagebroker.mapper.Mapper;
import com.andersenlab.messagebroker.mapper.annotation.Converter;
import com.andersenlab.messagebroker.pubsub.Publisher;
import com.andersenlab.messagebroker.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Converter
public class ProducerToPublisherConverter implements BaseConverter<Producer, Publisher> {

    @Autowired
    @Lazy
    private Mapper mapper;

    @Autowired
    private DestinationRepository destinationRepository;

    @Override
    public void convert(Producer from, Publisher to) {
        to.setName(from.getName());
        MsgDestination dest = from.getDestination() instanceof Queue
                ? mapper.map(from.getDestination(), MsgQueue.class)
                : mapper.map(from.getDestination(), MsgTopic.class);
        to.setDestination(dest);
        to.setCreatedAt(from.getCreatedAt());
        to.setAddress(from.getAddress());
    }
}
