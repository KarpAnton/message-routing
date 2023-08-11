package com.andersenlab.messagebroker.controller;

import com.andersenlab.messagebroker.pubsub.Subscriber;
import feign.RequestLine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.andersenlab.messagebroker.controller.ContextPath.CONTEXT_PATH;

public interface SubscriberControllerApi {

    @PostMapping(value = CONTEXT_PATH + "/subscriber", consumes = "application/json")
    @RequestLine("POST " + CONTEXT_PATH + "/subscriber")
    ResponseEntity<Void> subscribe(@RequestBody Subscriber subscriber);

    @DeleteMapping(value = CONTEXT_PATH + "/subscriber/{subscriberName}", consumes = "application/json")
    @RequestLine("DELETE " + CONTEXT_PATH + "/subscriber/{subscriberName}")
    ResponseEntity<Void> unsubscribe(@PathVariable String subscriberName);
}
