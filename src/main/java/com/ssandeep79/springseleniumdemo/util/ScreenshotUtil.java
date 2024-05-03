package com.ssandeep79.springseleniumdemo.util;

import com.ssandeep79.springseleniumdemo.annotation.LazyAutowired;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;

@Lazy
@Component
public class ScreenshotUtil {
    @Value("${screenshot.path}")
    private String screenshotPath;

    @LazyAutowired
    private TakesScreenshot driver;

    public void takeScreenshot(String filename) throws IOException {
        File scrFile = driver.getScreenshotAs(OutputType.FILE);
        FileCopyUtils.copy(scrFile, new File(screenshotPath, filename));
    }
    
}
