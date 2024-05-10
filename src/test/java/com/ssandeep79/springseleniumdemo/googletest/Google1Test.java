package com.ssandeep79.springseleniumdemo.googletest;

import com.ssandeep79.springseleniumdemo.SpringBaseTestNGTest;
import com.ssandeep79.springseleniumdemo.demo.annotation.LazyAutowired;
import com.ssandeep79.springseleniumdemo.demo.service.ScreenshotService;
import com.ssandeep79.springseleniumdemo.page.google.GooglePage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class Google1Test extends SpringBaseTestNGTest {

    @LazyAutowired
    private GooglePage googlePage;
    @LazyAutowired
    private ScreenshotService screenshotService;

    @Test
    public void googleTest() {
        googlePage.goTo();
        Assert.assertTrue(googlePage.isAt());

        googlePage.getSearchComponent().search("Spring boot");
        Assert.assertTrue(googlePage.getSearchResults().isAt());
        Assert.assertTrue(googlePage.getSearchResults().getCount() > 2);
        try {
            screenshotService.takeScreenshot();
        } catch (IOException e) {
            e.printStackTrace();
        }
        googlePage.close();
    }
}
