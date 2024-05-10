package com.ssandeep79.springseleniumdemo.demo.config;


import com.ssandeep79.springseleniumdemo.demo.annotation.LazyConfiguration;
import com.ssandeep79.springseleniumdemo.demo.annotation.ThreadScopeBean;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;

import java.net.MalformedURLException;
import java.net.URL;

@LazyConfiguration
@Profile({"remote"})
public class RemoteWebDriverConfig {

    @Value("${application.url}")
    private URL url;

    @ThreadScopeBean
    @ConditionalOnProperty(name = "browser", havingValue = "firefox")
    public WebDriver remoteFirefoxDriver () throws MalformedURLException {
        return getRemoteWebDriver(new FirefoxOptions());
    }

    @ThreadScopeBean
    @ConditionalOnProperty(name = "browser", havingValue = "chrome", matchIfMissing = true)
    // This method should be right at the end of the class as it is a default bean
    public WebDriver remoteChromeDriver () throws MalformedURLException {
        return getRemoteWebDriver(new ChromeOptions());
    }

    private RemoteWebDriver getRemoteWebDriver (AbstractDriverOptions<?> options) throws MalformedURLException {
        return new RemoteWebDriver(url, options);
    }
}
