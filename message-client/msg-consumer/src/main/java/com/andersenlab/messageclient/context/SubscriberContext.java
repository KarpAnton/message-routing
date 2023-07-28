package com.andersenlab.messageclient.context;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.pubsub.Subscriber;

public class SubscriberContext {

    private static MsgDestination destination;
    private static String name;
    private static String address;

    private SubscriberContext() {

    }

    public static MsgDestination getDestination() {
        return destination;
    }

    public static void setDestination(String destination) {
        SubscriberContext.destination = MsgDestination.createDestination(destination);
    }

    public static void setDestination(MsgDestination destination) {
        SubscriberContext.destination = destination;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        SubscriberContext.name = name;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        SubscriberContext.address = address;
    }

    public static void setParameters(Subscriber subscriber) {
        setName(subscriber.getName());
        setAddress(subscriber.getAddress());
        setDestination(subscriber.getDestination());
    }
}
