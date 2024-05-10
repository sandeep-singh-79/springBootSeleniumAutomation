package com.ssandeep79.springseleniumdemo.demo.annotation;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Lazy
@Component
@Documented
@Target({ElementType.TYPE })
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Page {

}
