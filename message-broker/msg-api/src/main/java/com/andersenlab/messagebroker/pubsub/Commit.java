package com.andersenlab.messagebroker.pubsub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Commit {

    private String destinationName;
    private String consumerName;
    private List<String> messageCorrelationIds;

    public Commit(String destinationName, String consumerName, List<Message> processedMessages) {
        this.destinationName = destinationName;
        this.consumerName = consumerName;
        this.messageCorrelationIds = processedMessages == null
                ? Collections.emptyList()
                : processedMessages.stream().map(Message::getCorrelationId).collect(Collectors.toList());
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
        return messageCorrelationIds.size();
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public List<String> getMessageCorrelationIds() {
        return messageCorrelationIds;
    }

    public void setMessageCorrelationIds(List<String> messageCorrelationIds) {
        this.messageCorrelationIds = messageCorrelationIds;
    }
}
