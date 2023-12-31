package com.andersenlab.messagebroker.converter;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.destination.MsgQueue;
import com.andersenlab.messagebroker.destination.MsgTopic;
import com.andersenlab.messagebroker.entity.Message;
import com.andersenlab.messagebroker.entity.Queue;
import com.andersenlab.messagebroker.pubsub.Publisher;
import org.akhome.mapper.Mapper;
import org.akhome.mapper.annotation.Converter;
import org.akhome.mapper.converter.BaseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Converter
public class MessageEntityToMessageConverter implements BaseConverter<Message, com.andersenlab.messagebroker.pubsub.Message> {

    @Autowired
    @Lazy
    private Mapper mapper;

    @Override
    public void convert(Message from, com.andersenlab.messagebroker.pubsub.Message to) {
        to.setId(from.getId());
        to.setPublisher(mapper.map(from.getProducer(), Publisher.class));
        to.setPayload(from.getPayload());
        to.setSentAt(from.getSentAt());
        to.setHeaders(from.getHeaders());
        MsgDestination dest = from.getDestination() instanceof Queue
                ? mapper.map(from.getDestination(), MsgQueue.class)
                : mapper.map(from.getDestination(), MsgTopic.class);
        to.setDestination(dest);
    }
}
