package com.andersenlab.messageclient.processor;

import com.andersenlab.messagebroker.pubsub.Message;

public interface MessageProcessor {

    void process(Message message) throws Exception;
}
