package com.andersenlab.messagebroker.repository;

import com.andersenlab.messagebroker.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

    boolean existsByName(String name);

    Consumer findByName(String name);
}
