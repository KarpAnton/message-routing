package com.andersenlab.messagebroker.repository;

import com.andersenlab.messagebroker.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {

    Destination findByName(String name);
}
