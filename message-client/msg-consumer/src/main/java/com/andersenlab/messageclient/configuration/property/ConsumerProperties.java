package com.andersenlab.messageclient.configuration.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "msg.consumer")
public class ConsumerProperties {

    private boolean enabledAutoCommit;

    private int batchSize;

    public boolean isEnabledAutoCommit() {
        return enabledAutoCommit;
    }

    public void setEnabledAutoCommit(boolean enabledAutoCommit) {
        this.enabledAutoCommit = enabledAutoCommit;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
