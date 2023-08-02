package com.andersenlab.messagebroker.repository;

import com.andersenlab.messagebroker.entity.Offset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OffsetRepository extends JpaRepository<Offset, Long> {

    @Query(value = "SELECT * FROM offsets WHERE o.consumer_id = :consumerId and o.destination_id = :destinationId",
            nativeQuery = true)
    Offset findByConsumerAndDestination(@Param("consumerId") Long consumerId,
                                        @Param("destinationId") Long destinationId);
}
