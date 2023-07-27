package com.andersenlab.messagebroker.controller;

import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messagebroker.pubsub.Publisher;
import com.andersenlab.messagebroker.pubsub.Subscriber;
import feign.RequestLine;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.andersenlab.messagebroker.controller.ContextPath.CONTEXT_PATH;

public interface BrokerControllerApi {

    @PostMapping(value = CONTEXT_PATH + "/destination/{destinationName}", consumes = "application/json")
    @RequestLine("POST " + CONTEXT_PATH + "/destination/{destinationName}")
    void registerDestination(@PathVariable String destinationName);

    @PostMapping(value = CONTEXT_PATH + "/message", consumes = "application/json")
    @RequestLine("POST " + CONTEXT_PATH + "/message")
    void sendMessage(@RequestBody Message message);

}
