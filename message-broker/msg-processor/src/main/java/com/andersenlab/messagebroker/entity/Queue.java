package com.andersenlab.messagebroker.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("QUEUE")
public class Queue extends Destination {

    public Queue() {
    }

    public Queue(String name) {
        super(name);
    }

}
