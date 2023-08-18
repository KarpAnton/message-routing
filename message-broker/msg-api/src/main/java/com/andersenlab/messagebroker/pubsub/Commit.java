package com.andersenlab.messagebroker.pubsub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Commit {

    private String destinationName;
    private String consumerName;
    private List<UUID> messageIds;

    public Commit(String destinationName, String consumerName, List<Message> processedMessages) {
        this.destinationName = destinationName;
        this.consumerName = consumerName;
        this.messageIds = processedMessages == null
                ? Collections.emptyList()
                : processedMessages.stream().map(Message::getId).collect(Collectors.toList());
    }

    public Commit() {
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public int getAcks() {
        return messageIds.size();
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public List<UUID> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(List<UUID> messageIds) {
        this.messageIds = messageIds;
    }
}
