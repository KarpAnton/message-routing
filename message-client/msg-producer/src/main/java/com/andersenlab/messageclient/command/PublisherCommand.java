package com.andersenlab.messageclient.command;

import com.andersenlab.messageclient.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class PublisherCommand{

    @Autowired
    private PublisherService publisherService;

    @ShellMethod(key = "set-producer", value = "Sets up producer parameters into context and sends them to broker")
    public void setProducerParameters(@ShellOption("--n") String producerName,
                                      @ShellOption("--dest") String destinationName) {

        publisherService.createPublisher(producerName, destinationName);
    }
}
