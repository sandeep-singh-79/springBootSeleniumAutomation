package com.ssandeep79.springseleniumdemo.config;


import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import com.ssandeep79.springseleniumdemo.annotation.LazyConfiguration;

@LazyConfiguration
@Profile({"remote"})
public class RemoteWebDriverConfig {
    @Value("${selenium.grid.url}")
    private URL url;

    @Bean
    @ConditionalOnProperty(name = "browser", havingValue = "firefox")
    public WebDriver remoteFirefoxDriver() {
        return new RemoteWebDriver(url, new FirefoxOptions());
    }

    @Bean
    @ConditionalOnMissingBean
    // This method should be right at the end of the class as it is a default bean
    public WebDriver remoteChromeDriver() {
        return new RemoteWebDriver(url, new ChromeOptions());
    }
}
