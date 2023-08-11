package com.andersenlab.messageclient.configuration;

import com.andersenlab.client.context.ClientContext;
import com.andersenlab.client.context.DefaultClientContext;
import com.andersenlab.messagebroker.pubsub.Subscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ConsumerConfiguration {

    @Bean
    public ClientContext<Subscriber> subscriberContext() {
        return new DefaultClientContext<>();
    }

    @Bean
    public ExecutorService singleThreadPoolExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
