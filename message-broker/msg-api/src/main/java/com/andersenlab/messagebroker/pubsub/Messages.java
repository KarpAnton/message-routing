package com.andersenlab.messagebroker.pubsub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Messages {

    private List<Message> messages;

    public Messages() {
    }

    public Messages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Message> getMessages() {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public boolean isEmpty() {
        return messages == null || messages.isEmpty();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public int size() {
        return isEmpty() ? 0 : messages.size();
    }

}
