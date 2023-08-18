package com.andersenlab.messagebroker.configuration;

import org.akhome.mapper.Mapper;
import org.akhome.mapper.MapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessorConfiguration {

    @Bean
    public Mapper mapper() {
        return new MapperImpl();
    }
}
