package com.ssandeep79.springseleniumdemo.config;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

@Lazy
@Profile("remote")
@Configuration
public class RemoteWebDriverConfig {
    @Value("${selenium.grid.url}")
    private URL url;

    @Bean
    @ConditionOnProperty(name = "browser", havingValue = "firefox")
    public WebDriver remoteFirefoxDriver() {
        return new RemoteWebDriver(url, new FirefoxOptions());
    }

    @Bean
    @ConditionOnMissingBean
    // This method should be right at the end of the class as it is a default bean
    public WebDriver remoteChromeDriver() {
        return new RemoteWebDriver(url, new ChromeOptions());
    }
}
