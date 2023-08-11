package com.andersenlab.messagebroker.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("TOPIC")
public class Topic extends Destination {

    public Topic() {
    }

    public Topic(String name) {
        super(name);
    }

}
