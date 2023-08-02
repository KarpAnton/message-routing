package com.andersenlab.messagebroker.service;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.entity.Destination;
import com.andersenlab.messagebroker.repository.DestinationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BrokerServiceTest {

    @Autowired
    private BrokerService brokerService;

    @Autowired
    private DestinationRepository destinationRepository;

    @ParameterizedTest
    @ValueSource(strings = {"queue-test-queue", "topic-test-topic"})
    public void shouldRegisterDestination(String destination) {
        MsgDestination msgDestination = MsgDestination.createDestination(destination);
        String destinationName = msgDestination.getName();
        brokerService.registerDestination(msgDestination);

        Destination foundDestination = destinationRepository.findByName(destinationName);

        Assertions.assertNotNull(foundDestination);
        Assertions.assertEquals(foundDestination.getName(), destinationName);
    }
}
