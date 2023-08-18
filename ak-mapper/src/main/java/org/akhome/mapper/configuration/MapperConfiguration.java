package org.akhome.mapper.configuration;

import org.akhome.mapper.Mapper;
import org.akhome.mapper.MapperImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class MapperConfiguration {

    @Bean
    public Mapper mapper() {
        return new MapperImpl();
    }
}
