package com.ssandeep79.springseleniumdemo.demo.annotation;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Lazy
@Component
@Documented
@Scope("prototype")
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PageFragment {

}
