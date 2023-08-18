package com.andersenlab.messagebroker.converter;

import com.andersenlab.messagebroker.destination.MsgTopic;
import com.andersenlab.messagebroker.entity.Topic;
import org.akhome.mapper.annotation.Converter;
import org.akhome.mapper.converter.BaseConverter;

@Converter
public class MsgTopicToTopicEntityConverter implements BaseConverter<MsgTopic, Topic> {

    @Override
    public void convert(MsgTopic from, Topic to) {
        to.setName(from.getName());
    }
}
