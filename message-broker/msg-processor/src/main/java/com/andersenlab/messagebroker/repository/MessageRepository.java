package com.andersenlab.messagebroker.repository;

import com.andersenlab.messagebroker.entity.Destination;
import com.andersenlab.messagebroker.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Used for extraction messages from topic
     * @param destination
     * @param pageable supports batching
     * @return list of messages
     */
    List<Message> findAllByDestination(Destination destination, Pageable pageable);

    /**
     * Extracts messages from a queue
     * @param destinationId
     * @param pageable
     * @return
     */
    // query will be extracted from NamedQuery Message.findAllByDestinationAndIsSentFalse
    List<Message> findAllByDestinationAndIsSentFalse(@Param("destinationId") Long destinationId, Pageable pageable);

    void deleteByCorrelationIdIn(List<String> correlationIds);

}
