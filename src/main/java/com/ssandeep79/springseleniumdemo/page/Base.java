package com.ssandeep79.springseleniumdemo.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ssandeep79.springseleniumdemo.annotation.LazyAutowired;

import jakarta.annotation.PostConstruct;

public abstract class Base {
    @LazyAutowired
    protected WebDriver driver;

    @LazyAutowired
    protected WebDriverWait wait;

    @PostConstruct
    protected void init() {
        PageFactory.initElements(driver, this);
    }

    public abstract boolean isAt();
}