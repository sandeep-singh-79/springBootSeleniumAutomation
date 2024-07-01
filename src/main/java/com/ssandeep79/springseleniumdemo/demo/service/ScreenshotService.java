package com.ssandeep79.springseleniumdemo.demo.service;

import com.github.javafaker.Faker;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Lazy
@Service
public class ScreenshotService {
    @Value("${screenshot.path}")
    private Path screenshotPath;

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private Faker faker;

    public void takeScreenshot (String filename) throws IOException {
        // Autowired and used ApplicationContext to handle the bean scope issue with ScreenshotService takeScreenshot method.
        File scrFile = ctx.getBean(TakesScreenshot.class).getScreenshotAs(OutputType.FILE);
        Path outFile = screenshotPath.resolve(filename);
        File parentDir = outFile.getParent().toFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        FileCopyUtils.copy(scrFile, outFile.toFile());
    }

    public void takeScreenshot () throws IOException {
        var fileName = faker.file().fileName((String) null,
            String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+05:30"))),
            "png",
            (String) null);
        takeScreenshot(fileName);
    }

    public byte[] getScreenshot() {
        return ctx.getBean(TakesScreenshot.class).getScreenshotAs(OutputType.BYTES);
    }

}
