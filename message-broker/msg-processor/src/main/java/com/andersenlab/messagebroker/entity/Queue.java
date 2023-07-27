package com.andersenlab.messagebroker.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("QUEUE")
public class Queue extends Destination {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "destination_consumers",
            joinColumns = @JoinColumn(name = "destination_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "consumer_id", referencedColumnName = "id")
    )
    private Consumer consumer;

    public Queue() {
    }

    public Queue(String name) {
        super(name);
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
}
