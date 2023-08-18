package com.andersenlab.messageclient.processor;

import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messageclient.entity.MessageMetadata;
import com.andersenlab.messageclient.repository.MessageMetadataRepository;
import org.akhome.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;

@Component
public class DefaultMessageProcessor implements MessageProcessor {

    private static final String PAYLOAD_PATTERN = "%tF %tT : [Message from %s] - %s";

    @Autowired
    private MessageMetadataRepository messageMetadataRepository;

    @Autowired
    private Mapper mapper;

    @Override
    @Transactional
    public void process(Message message) { // idempotent consumer
        MessageMetadata messageMetadata = mapper.map(message, MessageMetadata.class);
        if (!messageMetadataRepository.existsById(message.getId())) {
            messageMetadataRepository.save(messageMetadata);
            System.out.println(formattedPayload(message));
            System.out.println();
        }
    }

    private String formattedPayload(Message message) {
        Date messageSentAt = Date.from(message.getSentAt().atZone(ZoneId.systemDefault()).toInstant());
        return String.format(PAYLOAD_PATTERN,
                messageSentAt,
                messageSentAt,
                message.getPublisher().getName(),
                message.getPayload());
    }
}
