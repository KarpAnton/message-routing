package com.andersenlab.messagebroker.mapper.converter;

import com.andersenlab.messagebroker.destination.MsgQueue;
import com.andersenlab.messagebroker.entity.Queue;
import com.andersenlab.messagebroker.mapper.annotation.Converter;

@Converter
public class MsgQueueToQueueEntityConverter implements BaseConverter<MsgQueue, Queue> {

    @Override
    public void convert(MsgQueue from, Queue to) {
        to.setName(from.getName());
    }
}