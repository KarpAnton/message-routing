package com.andersenlab.messageclient.configuration;

import com.andersenlab.client.context.ClientContext;
import com.andersenlab.client.context.DefaultClientContext;
import com.andersenlab.messagebroker.pubsub.Subscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfiguration {

    @Bean
    public ClientContext<Subscriber> subscriberContext() {
        return new DefaultClientContext<>();
    }
}
