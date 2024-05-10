package com.ssandeep79.springseleniumdemo.util;

import com.github.javafaker.Faker;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Lazy
@Component
public class ScreenshotUtil {
    @Value("${screenshot.path}")
    private Path screenshotPath;

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private Faker faker;

    public void takeScreenshot (String filename) throws IOException {
        // Autowired and used ApplicationContext to handle the bean scope issue with ScreenshotUtil takeScreenshot method.
        File scrFile = ctx.getBean(TakesScreenshot.class).getScreenshotAs(OutputType.FILE);
        FileCopyUtils.copy(scrFile, screenshotPath.resolve(filename).toFile());
    }

    public void takeScreenshot () throws IOException {
        var fileName = faker.name().firstName() + LocalDateTime.now().toEpochSecond(ZoneOffset.of("+05:30")) + ".png";
        takeScreenshot(fileName);
    }

}
