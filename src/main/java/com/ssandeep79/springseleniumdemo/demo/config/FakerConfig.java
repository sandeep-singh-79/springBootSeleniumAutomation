package com.ssandeep79.springseleniumdemo.demo.config;

import com.github.javafaker.Faker;
import com.ssandeep79.springseleniumdemo.demo.annotation.LazyConfiguration;
import org.springframework.context.annotation.Bean;

@LazyConfiguration
public class FakerConfig {
    @Bean
    public Faker faker() {
        return new Faker();
    }
}