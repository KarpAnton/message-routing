package com.andersenlab.messageclient.command;

import com.andersenlab.messageclient.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class MessagingCommand {

    private static final Logger LOG = LoggerFactory.getLogger(MessagingCommand.class);

    @Autowired
    private MessageService messageService;

    @ShellMethod(key = "send", value = "Sends message to specified destination either topic or queue")
    public void sendMessage(@ShellOption({"--m"}) String payload,
                            @ShellOption(value = {"--d"}, defaultValue = "") String destination) {

        LOG.info("Sending message \"{}\" to {} destination", payload, destination);
        messageService.sendMessage(payload, destination);
    }
}
