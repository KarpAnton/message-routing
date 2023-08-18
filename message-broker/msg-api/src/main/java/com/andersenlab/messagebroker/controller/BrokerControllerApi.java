package com.andersenlab.messagebroker.controller;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.pubsub.Commit;
import com.andersenlab.messagebroker.pubsub.Message;
import com.andersenlab.messagebroker.pubsub.Messages;
import feign.RequestLine;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.andersenlab.messagebroker.controller.ContextPath.CONTEXT_PATH;

public interface BrokerControllerApi {

    @PostMapping(value = CONTEXT_PATH + "/destination", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestLine("POST " + CONTEXT_PATH + "/destination")
    void registerDestination(@RequestBody MsgDestination destination);

    @PostMapping(value = CONTEXT_PATH + "/message", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestLine("POST " + CONTEXT_PATH + "/message")
    Message sendMessage(@RequestBody Message message);

    @GetMapping(value = CONTEXT_PATH + "/messages/consumer/{consumerName}")
    @RequestLine("GET " + CONTEXT_PATH + "/messages/consumer/{consumerName}")
    Messages requestAvailableMessages(@PathVariable String consumerName,
                                      @RequestParam Integer batchSize);

    @PutMapping(value = CONTEXT_PATH + "/message/commitment", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestLine("PUT " + CONTEXT_PATH + "/message/commitment")
    void commitMessages(@RequestBody Commit commit);
}
