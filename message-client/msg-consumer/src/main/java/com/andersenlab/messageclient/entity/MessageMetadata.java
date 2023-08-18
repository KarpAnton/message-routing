package com.andersenlab.messageclient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "message_metadata")
public class MessageMetadata {

    @Id
    private UUID messageId;

    @Column(name = "producer_name")
    private String producerName;

    @Column(name = "destination_name")
    private String destinationName;

    @Column(name = "message_created_at")
    private LocalDateTime messageCreatedAt;

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public LocalDateTime getMessageCreatedAt() {
        return messageCreatedAt;
    }

    public void setMessageCreatedAt(LocalDateTime messageCreatedAt) {
        this.messageCreatedAt = messageCreatedAt;
    }
}
