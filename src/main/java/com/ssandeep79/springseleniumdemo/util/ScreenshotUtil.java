package com.ssandeep79.springseleniumdemo.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component public class ScreenshotUtil {
    @Value("${screenshot.path}")
    private Path path;

    @Autowired
    private TakesScreenshot driver;

    public void takeScreenshot(String filename) throws IOException {
        File scrFile = driver.getScreenshotAs(OutputType.FILE);
        FileCopyUtils.copy(scrFile, path.resolve(filename).toFile());
    }
    
}
