package com.ssandeep79.springseleniumdemo.bdd;

import com.ssandeep79.springseleniumdemo.demo.service.ScreenshotService;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;

public class CucumberHooks {
    private final Logger log = LoggerFactory.getLogger(CucumberHooks.class);

    @Autowired
    private ScreenshotService screenshotService;
    @Autowired
    private ApplicationContext ctx;

    @AfterStep
    public void afterStep (Scenario scenario) {
        if (scenario.isFailed()) {
            scenario.attach(screenshotService.getScreenshot(), "image/png", scenario.getName() + LocalDate.now());
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        log.info("Scenario '{}' - {}", scenario.getName(), scenario.getStatus());
        var webDriver = ctx.getBean(WebDriver.class);
        if (webDriver != null) {
            log.info("Closing the browser");
            webDriver.quit();
        }
    }
}
