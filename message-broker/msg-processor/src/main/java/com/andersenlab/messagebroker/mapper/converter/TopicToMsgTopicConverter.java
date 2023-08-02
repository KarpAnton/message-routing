package com.andersenlab.messagebroker.mapper.converter;

import com.andersenlab.messagebroker.destination.MsgTopic;
import com.andersenlab.messagebroker.entity.Topic;
import com.andersenlab.messagebroker.mapper.annotation.Converter;

@Converter
public class TopicToMsgTopicConverter implements BaseConverter<Topic, MsgTopic> {
    @Override
    public void convert(Topic from, MsgTopic to) {
        to.setName(from.getName());
    }
}
