package com.andersenlab.messageclient.command;

import com.andersenlab.client.utils.AddressUtils;
import com.andersenlab.messagebroker.controller.PublisherControllerApi;
import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.pubsub.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.LocalDateTime;

@ShellComponent
public class PublisherCommand extends BaseCommand {

    @Autowired
    private PublisherControllerApi publisherControllerApi;

    @ShellMethod(key = "set-producer", value = "Sets up producer parameters into context and sends them to broker")
    public void setProducerParameters(@ShellOption("--n") String producerName,
                                      @ShellOption("--dest") String destinationName) {

        Publisher publisher = new Publisher();
        publisher.setName(producerName);
        publisher.setAddress(AddressUtils.extractCurAddress());
        publisher.setDestination(MsgDestination.createDestination(destinationName, null));

        publisherContext.setClient(publisher);

        publisherControllerApi.register(publisher);
    }
}
