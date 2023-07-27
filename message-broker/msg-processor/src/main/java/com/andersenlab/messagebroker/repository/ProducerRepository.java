package com.andersenlab.messagebroker.repository;

import com.andersenlab.messagebroker.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {

    Producer findByName(String name);
}
