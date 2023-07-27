package com.andersenlab.messagebroker.mapper.converter;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.destination.MsgQueue;
import com.andersenlab.messagebroker.entity.Consumer;
import com.andersenlab.messagebroker.entity.Destination;
import com.andersenlab.messagebroker.entity.Queue;
import com.andersenlab.messagebroker.entity.Topic;
import com.andersenlab.messagebroker.mapper.Mapper;
import com.andersenlab.messagebroker.mapper.annotation.Converter;
import com.andersenlab.messagebroker.pubsub.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Converter
public class SubscriberToConsumerEntityConverter implements BaseConverter<Subscriber, Consumer> {

    @Autowired
    @Lazy
    private Mapper mapper;

    @Override
    public void convert(Subscriber from, Consumer to) {
        to.setName(from.getName());
        to.setAddress(from.getAddress());
        MsgDestination dest = from.getDestination();
        Destination destEntity = dest instanceof MsgQueue
                ? mapper.map(dest, Queue.class)
                : mapper.map(dest, Topic.class);
        to.setDestination(destEntity);
    }
}
