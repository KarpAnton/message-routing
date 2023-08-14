package com.andersenlab.messagebroker.controller;

import com.andersenlab.messagebroker.pubsub.Subscriber;
import feign.RequestLine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.andersenlab.messagebroker.controller.ContextPath.CONTEXT_PATH;

public interface SubscriberControllerApi {

    /**
     * Subscribes a listener to specified destination (topic or queue)
     * @param subscriber contains all required information related to subscriber (name, destination, etc.)
     * @return
     */
    @PostMapping(value = CONTEXT_PATH + "/subscriber", consumes = "application/json")
    @RequestLine("POST " + CONTEXT_PATH + "/subscriber")
    ResponseEntity<Void> subscribe(@RequestBody Subscriber subscriber);

    /**
     * Unsubscribes listener from a destination and removes it
     * @param subscriberName name of subscriber
     * @return
     */
    @DeleteMapping(value = CONTEXT_PATH + "/subscriber/{subscriberName}", consumes = "application/json")
    @RequestLine("DELETE " + CONTEXT_PATH + "/subscriber/{subscriberName}")
    ResponseEntity<Void> unsubscribe(@PathVariable String subscriberName);

    /**
     * Detaches subscriber from any destination.
     * As result, consumer doesn't listen to any destination (topic or queue).
     * To subscribe to any destination it is required to invoke {@link SubscriberControllerApi.subscribe} method
     * @param subscriberName name of subscriber to be detached
     */
    @PutMapping(value = CONTEXT_PATH + "subscriber/{subscriberName}", consumes = "application/json")
    @RequestLine("PUT " + CONTEXT_PATH + "/subscriber/{subscriberName}")
    void detach(@PathVariable String subscriberName);
}
