package com.andersenlab.messagebroker.controller;

import com.andersenlab.messagebroker.pubsub.Message;
import feign.RequestLine;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.andersenlab.messagebroker.controller.ContextPath.CONTEXT_PATH;

public interface BrokerControllerApi {

    @PostMapping(value = CONTEXT_PATH + "/destination/{destinationName}", consumes = "application/json")
    @RequestLine("POST " + CONTEXT_PATH + "/destination/{destinationName}")
    void registerDestination(@PathVariable String destinationName);

    @PostMapping(value = CONTEXT_PATH + "/message", consumes = "application/json")
    @RequestLine("POST " + CONTEXT_PATH + "/message")
    void sendMessage(@RequestBody Message message);

    @GetMapping(value = CONTEXT_PATH + "/messages/consumer/{consumerName}/destination/{destinationName}")
    @RequestLine("GET " + CONTEXT_PATH + "/messages/consumer/{consumerName}/destination/{destinationName}")
    List<Message> requestAvailableMessages(@PathVariable String consumerName,
                                           @RequestParam Integer batchSize);

    @PostMapping(value = CONTEXT_PATH + "/message/{correlationId}/consumer/{consumerName}")
    @RequestLine("POST " + CONTEXT_PATH + "/message/{correlationId}/consumer/{consumerName}")
    void commitMessage(@PathVariable String correlationId,
                       @PathVariable String consumerName);
}
