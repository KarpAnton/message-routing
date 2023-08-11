package com.andersenlab.messagebroker.destination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class MsgDestination {

    private static final String QUEUE_PREFIX = "queue-";
    private static final String TOPIC_PREFIX = "topic-";

    private String name;

    private DestinationType type;

    public MsgDestination(DestinationType type) {
        this.type = type;
    }

    public MsgDestination(String name, DestinationType type) {
        this.name = name;
        this.type = type;
    }

    @JsonCreator
    public static MsgDestination createDestination(@JsonProperty("name") String name, @JsonProperty("type") DestinationType type) {
        if (name == null) {
            throw new NullPointerException("Destination name should not have null value");
        }
        if (name.startsWith(QUEUE_PREFIX)) {
            return new MsgQueue(name.substring(QUEUE_PREFIX.length()));
        } else if (name.startsWith(TOPIC_PREFIX)) {
            return new MsgTopic(name.substring(TOPIC_PREFIX.length()));
        }

        switch (type) {
            case QUEUE -> {
                return new MsgQueue(name);
            }
            case TOPIC -> {
                return new MsgTopic(name);
            }
            default -> throw new IllegalArgumentException("Invalid destination type: " + type);
        }
    }

    public DestinationType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
