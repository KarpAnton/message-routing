package com.andersenlab.messagebroker.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "destinations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class Destination extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "destination")
    @OrderBy("createdAt ASC")
    private List<Message> messages;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "destination")
    private List<Offset> offsets;

    protected Destination() {
    }

    protected Destination(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Offset> getOffsets() {
        if (offsets == null) {
            offsets = new ArrayList<>();
        }
        return offsets;
    }

    public void setOffsets(List<Offset> offsets) {
        this.offsets = offsets;
    }
}
