package com.andersenlab.messageclient.context;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.pubsub.Publisher;

public class PublisherContext {

    private static MsgDestination destination;
    private static String name;
    private static String address;

    private PublisherContext() {

    }

    public static MsgDestination getDestination() {
        return destination;
    }

    public static void setDestination(String destination) {
        PublisherContext.destination = MsgDestination.createDestination(destination);
    }

    public static void setDestination(MsgDestination destination) {
        PublisherContext.destination = destination;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        PublisherContext.name = name;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        PublisherContext.address = address;
    }

    public static void setParameters(Publisher publisher) {
        setName(publisher.getName());
        setAddress(publisher.getAddress());
        setDestination(publisher.getDestination());
    }
}