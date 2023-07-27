package com.andersenlab.messageclient.configuration;

import com.andersenlab.messagebroker.controller.BrokerControllerApi;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    @Value("${msg.broker.url}")
    private String msgBrokerUrl;

    @Bean
    public BrokerControllerApi brokerClient() {
        return Feign.builder()
                .contract(new SpringMvcContract())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(BrokerControllerApi.class, msgBrokerUrl);
    }
}
