package com.ssandeep79.springseleniumdemo.demo.aspect;

import com.ssandeep79.springseleniumdemo.demo.annotation.TakeScreenshot;
import com.ssandeep79.springseleniumdemo.demo.service.ScreenshotService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Aspect
@Slf4j
@Service
public class ScreenshotAspect {
    @Autowired
    private ScreenshotService screenshotService;

    @After("@annotation(takeScreenshot)")
    public void after (TakeScreenshot takeScreenshot) throws IOException {
        screenshotService.takeScreenshot();
    }
}
