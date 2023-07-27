package com.andersenlab.messageclient.command;

import com.andersenlab.messagebroker.controller.BrokerControllerApi;
import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messagebroker.pubsub.Publisher;
import com.andersenlab.messageclient.context.PublisherContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.LocalDateTime;

@ShellComponent
public class MessagingCommand {

    @Autowired
    private BrokerControllerApi brokerControllerApi;

    @ShellMethod(key = "send", value = "Sends message to specified destination either topic or queue")
    public void sendMessage(@ShellOption({"-m"}) String payload,
                            @ShellOption({"-d"}) String destination) {

        Message message = new Message();
        message.setPayload(payload);
        message.setSentAt(LocalDateTime.now());

        message.setDestination(createDestination(destination));
        message.setPublisher(createPublisher());

        brokerControllerApi.sendMessage(message);
    }

    private MsgDestination createDestination(String destination) {
        if (StringUtils.isBlank(destination)) {
            return PublisherContext.getDestination();
        }
        if (StringUtils.isBlank(destination)) {
            throw new IllegalArgumentException("Destination is not present");
        }

       return MsgDestination.createDestination(destination);
    }

    private Publisher createPublisher() {
        Publisher publisher = new Publisher();
        publisher.setName(PublisherContext.getName());
        publisher.setAddress(PublisherContext.getAddress());
        return publisher;
    }
}
