package com.andersenlab.messageclient.command;

import com.andersenlab.messagebroker.controller.BrokerControllerApi;
import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.pubsub.Publisher;
import com.andersenlab.messageclient.context.PublisherContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@ShellComponent
public class PublisherContextCommand {

    @Autowired
    private BrokerControllerApi brokerControllerApi;

    @ShellMethod(key = "set-producer", value = "Sets producer parameters into context and sends them to broker")
    public void setProducerParameters(@ShellOption("-n") String producerName,
                                      @ShellOption("-dest") String destinationName) {

        Publisher publisher = new Publisher();
        publisher.setName(producerName);
        publisher.setAddress(extractAddress());
        publisher.setDestination(MsgDestination.createDestination(destinationName));
        publisher.setCreatedAt(LocalDateTime.now());

        PublisherContext.setParameters(publisher);

        brokerControllerApi.registerProducer(publisher);
    }

    private String extractAddress() {
        try {
            String port = System.getenv("server.port");
            return "http://" +
                    InetAddress.getLocalHost().getHostAddress() +
                    ":" +
                    port;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
