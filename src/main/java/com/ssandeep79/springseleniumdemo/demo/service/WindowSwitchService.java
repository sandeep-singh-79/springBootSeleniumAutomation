package com.ssandeep79.springseleniumdemo.demo.service;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class WindowSwitchService {

    @Autowired
    private ApplicationContext applicationContext;

    public void switchByTitle(final String title) {
        WebDriver driver = applicationContext.getBean(WebDriver.class);
        driver.getWindowHandles()
            .stream()
            .map(handle -> driver.switchTo().window(handle).getTitle())
            .filter(handle -> handle.startsWith(title))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Window with title %s not found".formatted(title)));
    }

    public void switchByIndex(final int index) {
        WebDriver driver = applicationContext.getBean(WebDriver.class);
        var windowHandles = driver.getWindowHandles().toArray(new String[0]);
        if (index >= windowHandles.length) {
            throw new RuntimeException("Index %s is greater than the number of windows".formatted(index));
        }
        driver.switchTo().window(windowHandles[index]);
    }
}
