package com.ssandeep79.springseleniumdemo.googletest;

import com.ssandeep79.springseleniumdemo.SpringBaseTestNGTest;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.testng.annotations.Test;

public class MultiBrowserTest extends SpringBaseTestNGTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void browserTest() {
        var chromeDriver = applicationContext.getBean("chromeDriver", WebDriver.class);
        chromeDriver.get("https://www.google.com");
        var firefoxDriver = applicationContext.getBean("firefoxDriver", WebDriver.class);
        firefoxDriver.get("https://www.google.com");

        chromeDriver.quit();
        firefoxDriver.quit();
    }
}
