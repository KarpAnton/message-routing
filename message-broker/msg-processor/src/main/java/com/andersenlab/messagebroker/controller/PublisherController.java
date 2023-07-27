package com.andersenlab.messagebroker.controller;

import com.andersenlab.messagebroker.pubsub.Publisher;
import com.andersenlab.messagebroker.service.PublisherService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublisherController implements PublisherControllerApi {

    private final static Logger LOG = LoggerFactory.getLogger(PublisherController.class);

    @Autowired
    private PublisherService publisherService;

    @Override
    public void register(Publisher publisher) {
        if (publisher == null) {
            throw new NullPointerException("Publisher object cannot be null");
        }
        LOG.info("Registering a new publisher {}", publisher.getName());
        publisherService.register(publisher);
        LOG.info("Publisher {} has been registered successfully", publisher.getName());
    }

    @Override
    public void unregister(String publisherName) {
        if (StringUtils.isBlank(publisherName)) {
            throw new NullPointerException("Publisher name cannot be null or empty");
        }
        LOG.info("Removing publisher {} from the broker", publisherName);
        publisherService.unregister(publisherName);
        LOG.info("Publisher {} has been removed successfully", publisherName);
    }
}
