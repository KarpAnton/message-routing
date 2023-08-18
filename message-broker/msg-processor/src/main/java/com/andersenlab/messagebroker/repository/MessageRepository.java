package com.andersenlab.messagebroker.repository;

import com.andersenlab.messagebroker.entity.Destination;
import com.andersenlab.messagebroker.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

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
    @Query(value = "SELECT m FROM Message as m WHERE m.destination.id = :destinationId AND (m.requestedBy.id = :consumerId OR m.requestedBy = null)")
    List<Message> findAllByDestination(@Param("destinationId") Long destinationId, @Param("consumerId") Long consumerId, Pageable pageable);

    /**
     * Deletes messages by ids
     * @param ids ids of messages
     */
    void deleteByIdIn(List<UUID> ids);

}
