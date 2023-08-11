package com.andersenlab.messageclient.command;

import com.andersenlab.messagebroker.controller.BrokerControllerApi;
import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messagebroker.pubsub.Publisher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.LocalDateTime;
import java.util.UUID;

@ShellComponent
public class MessagingCommand extends BaseCommand {

    private static final Logger LOG = LoggerFactory.getLogger(MessagingCommand.class);

    @Autowired
    private BrokerControllerApi brokerControllerApi;

    @ShellMethod(key = "send", value = "Sends message to specified destination either topic or queue")
    public void sendMessage(@ShellOption({"--m"}) String payload,
                            @ShellOption(value = {"--d"}, defaultValue = "") String destination) {

        Message message = new Message();
        message.setPayload(payload);
        message.setSentAt(LocalDateTime.now());
        message.setCorrelationId(UUID.randomUUID().toString());
        message.setDestination(createDestination(destination));
        message.setPublisher(createPublisher());

        brokerControllerApi.sendMessage(message);
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

    private Publisher createPublisher() {
        Publisher publisher = new Publisher();
        publisher.setName(publisherContext.getName());
        publisher.setAddress(publisherContext.getAddress());
        return publisher;
    }
}
