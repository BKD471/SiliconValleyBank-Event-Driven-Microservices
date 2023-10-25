package com.siliconvalley.accountsservices.configs;

import org.modelmapper.ModelMapper;
import org.modelmapper.record.RecordModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {
    @Bean
    public ModelMapper mapper() {
        return new ModelMapper().registerModule(new RecordModule());
    }
}
