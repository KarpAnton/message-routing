package com.andersenlab.messageclient.configuration;

import com.andersenlab.messagebroker.controller.BrokerControllerApi;
import com.andersenlab.messagebroker.controller.PublisherControllerApi;
import com.andersenlab.messagebroker.controller.SubscriberControllerApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Contract;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
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

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    private <T> T abstractClient(Class<T> tClass) {
        return Feign.builder()
                .contract(springMvcContract())
                .encoder(new JacksonEncoder(objectMapper()))
                .decoder(new JacksonDecoder(objectMapper()))
                .target(tClass, msgBrokerUrl);
    }
}
