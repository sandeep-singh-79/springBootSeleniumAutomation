package com.ssandeep79.springseleniumdemo.bdd;

import com.ssandeep79.springseleniumdemo.demo.service.ScreenshotService;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class CucumberHooks {

    @Autowired
    private ScreenshotService screenshotService;

    @AfterStep
    public void afterStep (Scenario scenario) {
        if (scenario.isFailed()) {
            scenario.embed(screenshotService.getScreenshot(), "image/png", scenario.getName() + LocalDate.now());
        }
    }
}
