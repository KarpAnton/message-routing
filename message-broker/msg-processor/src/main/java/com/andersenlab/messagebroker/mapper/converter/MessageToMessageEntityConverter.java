package com.andersenlab.messagebroker.mapper.converter;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.destination.MsgQueue;
import com.andersenlab.messagebroker.entity.Destination;
import com.andersenlab.messagebroker.entity.Queue;
import com.andersenlab.messagebroker.entity.Topic;
import com.andersenlab.messagebroker.mapper.Mapper;
import com.andersenlab.messagebroker.mapper.annotation.Converter;
import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messagebroker.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Converter
public class MessageToMessageEntityConverter implements BaseConverter<Message, com.andersenlab.messagebroker.entity.Message> {

    @Autowired
    @Lazy
    private Mapper mapper;

    @Autowired
    private DestinationRepository destinationRepository;

    @Override
    public void convert(Message from, com.andersenlab.messagebroker.entity.Message to) {
        to.setPayload(from.getPayload());
        to.setSentAt(from.getSentAt());
        to.setCorrelationId(from.getCorrelationId());
        MsgDestination dest = from.getDestination();
        Destination destEntity = destinationRepository.findByName(dest.getName());
        if (destEntity == null) {
            destEntity = dest instanceof MsgQueue
                    ? mapper.map(dest, Queue.class)
                    : mapper.map(dest, Topic.class);
        }
        to.setDestination(destEntity);
    }
}
