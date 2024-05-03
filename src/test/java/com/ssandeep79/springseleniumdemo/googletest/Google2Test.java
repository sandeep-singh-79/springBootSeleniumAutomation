package com.ssandeep79.springseleniumdemo.googletest;

import com.ssandeep79.springseleniumdemo.SpringBaseTestNGTest;
import com.ssandeep79.springseleniumdemo.annotation.LazyAutowired;
import com.ssandeep79.springseleniumdemo.page.google.GooglePage;
import com.ssandeep79.springseleniumdemo.util.ScreenshotUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Google2Test extends SpringBaseTestNGTest {

    @LazyAutowired
    private GooglePage googlePage;
    @LazyAutowired
    private ScreenshotUtil screenshotUtil;

    @Test
    public void googleTest() {
        googlePage.goTo();
        Assert.assertTrue(googlePage.isAt());

        googlePage.getSearchComponent().search("selenium");
        Assert.assertTrue(googlePage.getSearchResults().isAt());
        Assert.assertTrue(googlePage.getSearchResults().getCount() > 2);
        try {
            screenshotUtil.takeScreenshot(String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+05:30"))) + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
