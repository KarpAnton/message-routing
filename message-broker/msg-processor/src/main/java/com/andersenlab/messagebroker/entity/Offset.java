package com.andersenlab.messagebroker.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "offsets")
public class Offset extends BaseEntity {

    @OneToOne(mappedBy = "offset")
    private Consumer consumer;

    @ManyToOne(fetch = FetchType.EAGER)
    private Destination destination;

    @Column(name = "pos_pointer")
    @Basic
    private int posPointer;

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public int getPosPointer() {
        return posPointer;
    }

    public void setPosPointer(int posPointer) {
        this.posPointer = posPointer;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }
}
