package com.andersenlab.messagebroker.converter;

import com.andersenlab.messagebroker.destination.MsgTopic;
import com.andersenlab.messagebroker.entity.Topic;
import org.akhome.mapper.annotation.Converter;
import org.akhome.mapper.converter.BaseConverter;

@Converter
public class TopicToMsgTopicConverter implements BaseConverter<Topic, MsgTopic> {
    @Override
    public void convert(Topic from, MsgTopic to) {
        to.setName(from.getName());
    }
}
