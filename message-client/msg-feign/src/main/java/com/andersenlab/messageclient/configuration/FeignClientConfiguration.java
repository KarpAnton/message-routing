package com.andersenlab.messageclient.configuration;

import com.andersenlab.messagebroker.controller.BrokerControllerApi;
import com.andersenlab.messagebroker.controller.PublisherControllerApi;
import com.andersenlab.messagebroker.controller.SubscriberControllerApi;
import feign.Contract;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

    @Value("${msg.broker.url}")
    private String msgBrokerUrl;

    @Bean
    public Contract springMvcContract() {
        return new SpringMvcContract();
    }

    @Bean
    public BrokerControllerApi brokerClient() {
        return abstractClient(BrokerControllerApi.class);
    }

    @Bean
    public PublisherControllerApi publisherClient() {
        return abstractClient(PublisherControllerApi.class);
    }

    @Bean
    public SubscriberControllerApi subscriberClient() {
        return abstractClient(SubscriberControllerApi.class);
    }

    private <T> T abstractClient(Class<T> tClass) {
        return Feign.builder()
                .contract(springMvcContract())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(tClass, msgBrokerUrl);
    }
}
