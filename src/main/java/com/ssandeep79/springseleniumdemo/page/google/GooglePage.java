package com.ssandeep79.springseleniumdemo.page.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class GooglePage extends com.ssandeep79.springseleniumdemo.page.Base {
    @Value("${application.url}")
    private String url;

    @Autowired
    @Getter
    private SearchComponent searchComponent;
    @Autowired
    @Getter
    private SearchResults searchResults;

    @Override
    public boolean isAt() {
        return searchComponent.isAt();
    }

    public void goTo() {
        driver.get(url);
    }

}
