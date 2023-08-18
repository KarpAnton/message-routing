package com.andersenlab.messageclient.service;

import com.andersenlab.client.service.BaseService;
import com.andersenlab.messagebroker.controller.BrokerControllerApi;
import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messagebroker.pubsub.Publisher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MessageService extends BaseService<Publisher> {

    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private BrokerControllerApi brokerApi;

    public void sendMessage(String payload, String destination) {
        doSend(createMessage(payload, destination));
    }

    private void doSend(Message message) {

        brokerApi.sendMessage(message);
    }

    private Message createMessage(String payload, String destination) {
        Message message = new Message();
        message.setId(UUID.randomUUID());
        message.setPayload(payload);
        message.setSentAt(LocalDateTime.now());
        message.setDestination(createDestination(destination));
        message.setPublisher(publisherContext.getClient());

        return message;
    }

    private MsgDestination createDestination(String destination) {
        if (StringUtils.isBlank(destination)) {
            LOG.info("Extracted destination from the context");
            return publisherContext.getDestination();
        }
        if (StringUtils.isBlank(destination)) {
            throw new IllegalArgumentException("Destination is not present");
        }

        return MsgDestination.createDestination(destination, null);
    }
}
