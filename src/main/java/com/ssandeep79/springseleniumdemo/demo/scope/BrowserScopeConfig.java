package com.ssandeep79.springseleniumdemo.demo.scope;

import com.ssandeep79.springseleniumdemo.demo.annotation.LazyConfiguration;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;


@LazyConfiguration
public class BrowserScopeConfig {

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new BrowserScopePostProcessor();
    }

}
