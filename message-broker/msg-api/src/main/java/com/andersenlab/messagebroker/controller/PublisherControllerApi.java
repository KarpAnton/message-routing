package com.andersenlab.messagebroker.controller;

import com.andersenlab.messagebroker.pubsub.Publisher;
import feign.RequestLine;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.andersenlab.messagebroker.controller.ContextPath.CONTEXT_PATH;

public interface PublisherControllerApi {

    @PostMapping(value = CONTEXT_PATH + "/publisher", consumes = "application/json")
    @RequestLine("POST " + CONTEXT_PATH + "/publisher")
    void register(@RequestBody Publisher publisher);

    @DeleteMapping(value = CONTEXT_PATH + "/publisher/{publisherName}")
    @RequestLine("DELETE " + CONTEXT_PATH + "/publisher/{publisherName}")
    void unregister(@PathVariable String publisherName);
}
