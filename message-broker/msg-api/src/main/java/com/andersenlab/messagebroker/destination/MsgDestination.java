package com.andersenlab.messagebroker.destination;

public abstract class MsgDestination {

    private static final String QUEUE_PREFIX = "queue-";
    private static final String TOPIC_PREFIX = "topic-";

    protected String name;

    public MsgDestination(String name) {
        this.name = name;
    }

    public static MsgDestination createDestination(String name) {
        if (name == null) {
            throw new NullPointerException("Destination name should not have null value");
        }
        if (name.startsWith(QUEUE_PREFIX)) {
            return new MsgQueue(name.substring(QUEUE_PREFIX.length()));
        } else if (name.startsWith(TOPIC_PREFIX)) {
            return new MsgTopic(name.substring(TOPIC_PREFIX.length()));
        } else {
            throw new IllegalArgumentException("Invalid destination name: " + name);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
