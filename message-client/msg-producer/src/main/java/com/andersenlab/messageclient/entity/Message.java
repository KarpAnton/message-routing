package com.andersenlab.messageclient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    private UUID id;

    @Column(name = "payload")
    private String payload;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;
}
