package com.andersenlab.messagebroker.pubsub;

import com.andersenlab.messagebroker.destination.MsgDestination;

import java.time.LocalDateTime;
import java.util.Map;

public class Message {

    private String correlationId;

    private String payload;

    private Publisher publisher;

    private MsgDestination destination;

    private LocalDateTime sentAt;

    private Map<String, String> headers;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public MsgDestination getDestination() {
        return destination;
    }

    public void setDestination(MsgDestination destination) {
        this.destination = destination;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}
