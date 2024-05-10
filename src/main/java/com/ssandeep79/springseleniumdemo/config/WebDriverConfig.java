package com.ssandeep79.springseleniumdemo.config;

import com.ssandeep79.springseleniumdemo.annotation.LazyConfiguration;
import com.ssandeep79.springseleniumdemo.annotation.ThreadScopeBean;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;

@Profile({"!remote"})
@LazyConfiguration
public class WebDriverConfig {

   @ThreadScopeBean
    // comment the conditional on property annotation to run the test MultiBrowserTest
    @ConditionalOnProperty(name = "browser", havingValue = "firefox")
    public WebDriver firefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver();
    }

    @ThreadScopeBean
    // comment the conditional on property annotation to run the test MultiBrowserTest
    @ConditionalOnProperty(name = "browser", havingValue = "chrome", matchIfMissing = true)
    public WebDriver chromeDriver() {
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver();
    }
}