package com.rakesh.ebookstore.service;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//to register modelmapper as a bean, so that only one model mapper is used throughout application

//this annotation marks the class where it has bean definitions
@Configuration
public class ModelMapperConfig {
    
    //this annotation marks this method as benn producer
    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }

}
