package com.andersenlab.messagebroker.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("TOPIC")
public class Topic extends Destination {

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "destination_consumers",
            joinColumns = @JoinColumn(name = "destination_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "consumer_id", referencedColumnName = "id")
    )
    private List<Consumer> consumers;

    public Topic() {
    }

    public Topic(String name) {
        super(name);
    }

    public List<Consumer> getConsumers() {
        if (consumers == null) {
            consumers = new ArrayList<>();
        }
        return consumers;
    }

    public void setConsumers(List<Consumer> consumers) {
        this.consumers = consumers;
    }
}
