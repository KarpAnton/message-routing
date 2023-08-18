package com.andersenlab.messagebroker.converter;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.destination.MsgQueue;
import com.andersenlab.messagebroker.entity.Destination;
import com.andersenlab.messagebroker.entity.Producer;
import com.andersenlab.messagebroker.entity.Queue;
import com.andersenlab.messagebroker.entity.Topic;
import com.andersenlab.messagebroker.pubsub.Publisher;
import com.andersenlab.messagebroker.repository.DestinationRepository;
import org.akhome.mapper.Mapper;
import org.akhome.mapper.annotation.Converter;
import org.akhome.mapper.converter.BaseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Converter
public class PublisherToProducerEntityConverter implements BaseConverter<Publisher, Producer> {

    @Autowired
    @Lazy
    private Mapper mapper;

    @Autowired
    private DestinationRepository destinationRepository;

    @Override
    public void convert(Publisher from, Producer to) {
        to.setAddress(from.getAddress());
        to.setName(from.getName());
        to.setCreatedAt(from.getCreatedAt());
        MsgDestination destination = from.getDestination();
        Destination destinationEntity = destinationRepository.findByName(destination.getName());
        if (destinationEntity == null) {
            destinationEntity = destination instanceof MsgQueue
                    ? mapper.map(destination, Queue.class)
                    : mapper.map(destination, Topic.class);
        }
        to.setDestination(destinationEntity);
    }
}
