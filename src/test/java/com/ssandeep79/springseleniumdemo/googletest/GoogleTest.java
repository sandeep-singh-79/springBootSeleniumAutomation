package com.ssandeep79.springseleniumdemo.googletest;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ssandeep79.springseleniumdemo.SpringBaseTestNGTest;
import com.ssandeep79.springseleniumdemo.page.google.GooglePage;
import com.ssandeep79.springseleniumdemo.util.ScreenshotUtil;

public class GoogleTest extends SpringBaseTestNGTest {

    @Autowired
    private GooglePage googlePage;
    @Autowired
    private ScreenshotUtil screenshotUtil;

    @Test
    public void googleTest() {
        googlePage.goTo();
        Assert.assertTrue(googlePage.isAt());

        googlePage.getSearchComponent().search("Spring boot");
        Assert.assertTrue(googlePage.getSearchResults().isAt());
        Assert.assertTrue(googlePage.getSearchResults().getCount() > 2);
        screenshotUtil.takeScreenshot(String.valueOf(LocalDateTime.now().toEpochSecond()));
    }

}
