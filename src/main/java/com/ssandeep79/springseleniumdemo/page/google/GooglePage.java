package com.ssandeep79.springseleniumdemo.page.google;

import com.ssandeep79.springseleniumdemo.annotation.LazyAutowired;
import com.ssandeep79.springseleniumdemo.annotation.Page;
import com.ssandeep79.springseleniumdemo.page.Base;
import org.springframework.beans.factory.annotation.Value;

@Page
public class GooglePage extends Base {
    @Value("${application.url}")
    private String url;

    @LazyAutowired
    private SearchComponent searchComponent;
    @LazyAutowired
    private SearchResults searchResults;


    @Override
    public boolean isAt () {
        return searchComponent.isAt();
    }

    public void goTo () {
        driver.get(url);
    }

    public SearchComponent getSearchComponent () {
        return searchComponent;
    }

    public SearchResults getSearchResults () {
        return searchResults;
    }

}
