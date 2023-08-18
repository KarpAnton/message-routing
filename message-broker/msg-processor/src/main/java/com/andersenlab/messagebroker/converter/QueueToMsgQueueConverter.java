package com.andersenlab.messagebroker.converter;

import com.andersenlab.messagebroker.destination.MsgQueue;
import com.andersenlab.messagebroker.entity.Queue;
import org.akhome.mapper.annotation.Converter;
import org.akhome.mapper.converter.BaseConverter;

@Converter
public class QueueToMsgQueueConverter implements BaseConverter<Queue, MsgQueue> {
    @Override
    public void convert(Queue from, MsgQueue to) {
        to.setName(from.getName());
    }
}
