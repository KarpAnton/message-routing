package com.andersenlab.messagebroker.mapper.converter;

import com.andersenlab.messagebroker.destination.MsgTopic;
import com.andersenlab.messagebroker.entity.Topic;
import com.andersenlab.messagebroker.mapper.annotation.Converter;

@Converter
public class MsgTopicToTopicEntityConverter implements BaseConverter<MsgTopic, Topic> {

    @Override
    public void convert(MsgTopic from, Topic to) {
        to.setName(from.getName());
    }
}
