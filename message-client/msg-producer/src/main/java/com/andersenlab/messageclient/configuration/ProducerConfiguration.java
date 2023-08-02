package com.andersenlab.messageclient.configuration;

import com.andersenlab.client.context.ClientContext;
import com.andersenlab.client.context.DefaultClientContext;
import com.andersenlab.messagebroker.pubsub.Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfiguration {

    @Bean
    public ClientContext<Publisher> publisherContext() {
        return new DefaultClientContext<>();
    }
}
