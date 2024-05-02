package com.ssandeep79.springseleniumdemo.page.google;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;

import com.ssandeep79.springseleniumdemo.annotation.LazyAutowired;
import com.ssandeep79.springseleniumdemo.annotation.Page;
import com.ssandeep79.springseleniumdemo.page.Base;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Page
@Slf4j
@ComponentScan("com.ssandeep79.springseleniumdemo.page")
public class GooglePage extends Base {
    Logger logger = LoggerFactory.getLogger(GooglePage.class);

    @Value("${application.url}")
    private String url;

    @Getter
    @LazyAutowired
    private SearchComponent searchComponent;
    @Getter
    @LazyAutowired
    private SearchResults searchResults;

    @PostConstruct
    protected void init() {
        super.init();
        log.info("url: {}", url);
    }

    @Override
    public boolean isAt() {
        return searchComponent.isAt();
    }

    public void goTo() {
        log.info("Navigating to {}", url);
        driver.get(url);
    }

}
