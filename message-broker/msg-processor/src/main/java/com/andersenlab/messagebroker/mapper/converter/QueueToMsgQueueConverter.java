package com.andersenlab.messagebroker.mapper.converter;

import com.andersenlab.messagebroker.destination.MsgQueue;
import com.andersenlab.messagebroker.entity.Queue;
import com.andersenlab.messagebroker.mapper.annotation.Converter;

@Converter
public class QueueToMsgQueueConverter implements BaseConverter<Queue, MsgQueue>  {
    @Override
    public void convert(Queue from, MsgQueue to) {
        to.setName(from.getName());
    }
}
