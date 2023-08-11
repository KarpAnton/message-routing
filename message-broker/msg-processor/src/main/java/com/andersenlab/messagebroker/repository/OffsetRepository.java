package com.andersenlab.messagebroker.repository;

import com.andersenlab.messagebroker.entity.Consumer;
import com.andersenlab.messagebroker.entity.Destination;
import com.andersenlab.messagebroker.entity.Offset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OffsetRepository extends JpaRepository<Offset, Long> {

    Offset findByConsumerAndDestination(Consumer consumer, Destination destination);
}
