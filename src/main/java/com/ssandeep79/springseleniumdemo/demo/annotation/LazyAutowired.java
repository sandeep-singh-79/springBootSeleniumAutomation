package com.ssandeep79.springseleniumdemo.demo.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.lang.annotation.*;

@Lazy
@Autowired
@Target({ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LazyAutowired {

}
