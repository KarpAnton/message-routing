package com.andersenlab.messageclient.repository;

import com.andersenlab.messageclient.entity.MessageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageMetadataRepository extends JpaRepository<MessageMetadata, UUID> {
}
