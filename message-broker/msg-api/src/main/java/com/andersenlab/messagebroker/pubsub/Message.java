package com.andersenlab.messagebroker.pubsub;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Message {

    private UUID id;

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
        if (headers == null) {
            headers = new HashMap<>();
        }
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCorrelationId(UUID correlationId) {
        getHeaders().put(HeaderKeys.CORRELATION_ID, correlationId.toString());
    }

    public UUID getCorrelationId() {
        String correlationId = getHeaders().get(HeaderKeys.CORRELATION_ID);
        return correlationId == null ? null : UUID.fromString(correlationId);
    }
}
