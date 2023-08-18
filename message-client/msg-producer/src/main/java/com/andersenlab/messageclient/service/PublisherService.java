package com.andersenlab.messageclient.service;

import com.andersenlab.client.service.BaseService;
import com.andersenlab.client.utils.AddressUtils;
import com.andersenlab.messagebroker.controller.PublisherControllerApi;
import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.pubsub.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherService extends BaseService<Publisher> {

    @Autowired
    private PublisherControllerApi publisherApi;

    public void createPublisher(String producerName, String destinationName) {
        Publisher publisher = new Publisher();
        publisher.setName(producerName);
        publisher.setAddress(AddressUtils.extractCurAddress());
        publisher.setDestination(MsgDestination.createDestination(destinationName, null));

        publisherContext.setClient(publisher);

        publisherApi.register(publisher);
    }
}
