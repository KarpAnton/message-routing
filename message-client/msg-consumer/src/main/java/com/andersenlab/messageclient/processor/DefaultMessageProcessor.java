package com.andersenlab.messageclient.processor;

import com.andersenlab.messagebroker.pubsub.Message;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;

@Component
public class DefaultMessageProcessor implements MessageProcessor {

    private static final String PAYLOAD_PATTERN = "%tF %tT : [Message from %s] - %s";

    @Override
    public void process(Message message) {
        System.out.println(formattedPayload(message));
        System.out.println();
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
