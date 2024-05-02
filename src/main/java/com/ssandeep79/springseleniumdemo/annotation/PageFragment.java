package com.ssandeep79.springseleniumdemo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Lazy
@Component
@Documented
@Scope("prototype")
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PageFragment {

}
