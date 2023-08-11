package com.andersenlab.messagebroker.mapper.converter;

import com.andersenlab.messagebroker.destination.MsgDestination;
import com.andersenlab.messagebroker.entity.Consumer;
import com.andersenlab.messagebroker.entity.Destination;
import com.andersenlab.messagebroker.entity.Offset;
import com.andersenlab.messagebroker.mapper.Mapper;
import com.andersenlab.messagebroker.mapper.annotation.Converter;
import com.andersenlab.messagebroker.exception.DestinationNotFoundException;
import com.andersenlab.messagebroker.pubsub.Subscriber;
import com.andersenlab.messagebroker.repository.DestinationRepository;
import com.andersenlab.messagebroker.repository.OffsetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.util.Objects;

@Converter
public class SubscriberToConsumerEntityConverter implements BaseConverter<Subscriber, Consumer> {

    @Autowired
    @Lazy
    private Mapper mapper;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private OffsetRepository offsetRepository;

    @Override
    public void convert(Subscriber from, Consumer to) {
        to.setName(from.getName());
        to.setAddress(from.getAddress());
        MsgDestination dest = from.getDestination();
        to.setCreatedAt(LocalDateTime.now());
        Destination foundDestination = destinationRepository.findByName(dest.getName());
        if (foundDestination != null) {
            to.setDestination(foundDestination);
            mapOffset(to);
        } else {
            throw new DestinationNotFoundException("Destination " + dest.getName() + " not found");
        }
    }

    private void mapOffset(Consumer to) {
        if (to.getId() != null) {
            Offset offset = offsetRepository.findByConsumerAndDestination(to, to.getDestination());
            to.setOffset(offset);
        } else {
            to.setOffset(createOffset(to));
        }
    }

    private Offset createOffset(Consumer to) {
        Offset offset = new Offset();
        offset.setConsumer(to);
        offset.setDestination(to.getDestination());

        return offset;
    }
}
