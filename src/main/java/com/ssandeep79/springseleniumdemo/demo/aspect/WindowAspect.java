package com.ssandeep79.springseleniumdemo.demo.aspect;

import com.ssandeep79.springseleniumdemo.demo.annotation.Window;
import com.ssandeep79.springseleniumdemo.demo.service.WindowSwitchService;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class WindowAspect {

    @Autowired
    private WindowSwitchService switchService;

    @Before("@target(window) && within(com.ssandeep79.springseleniumdemo..*)")
    public void before(Window window) {
        switchService.switchByTitle(window.value());
    }

    @After("@target(window) && within(com.ssandeep79.springseleniumdemo..*)")
    public void after(Window window) {
        switchService.switchByIndex(0);
    }
}
