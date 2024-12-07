package com.home.petstore.config;

import com.home.petstore.util.ExtendedModelMapper;
import org.springframework.context.annotation.Bean;

public class ModelMapperConfig
{
    @Bean
    public ExtendedModelMapper modelMapper() {
        return new ExtendedModelMapper();
    }
}
