package com.andersenlab.messageclient.converter;

import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messageclient.entity.MessageMetadata;
import org.akhome.mapper.annotation.Converter;
import org.akhome.mapper.converter.BaseConverter;

@Converter
public class MessageToMessageMetaDataConverter implements BaseConverter<Message, MessageMetadata> {
    @Override
    public void convert(Message from, MessageMetadata to) {
        to.setMessageId(from.getId());
        to.setDestinationName(from.getDestination().getName());
        to.setProducerName(from.getPublisher().getName());
        to.setMessageCreatedAt(from.getSentAt());
    }
}
