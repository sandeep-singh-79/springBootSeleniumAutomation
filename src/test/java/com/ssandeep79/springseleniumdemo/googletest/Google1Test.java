package com.ssandeep79.springseleniumdemo.googletest;

import com.ssandeep79.springseleniumdemo.SpringBaseTestNGTest;
import com.ssandeep79.springseleniumdemo.annotation.LazyAutowired;
import com.ssandeep79.springseleniumdemo.page.google.GooglePage;
import com.ssandeep79.springseleniumdemo.util.ScreenshotUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class Google1Test extends SpringBaseTestNGTest {

    @LazyAutowired
    private GooglePage googlePage;
    @LazyAutowired
    private ScreenshotUtil screenshotUtil;

    @Test
    public void googleTest() {
        googlePage.goTo();
        Assert.assertTrue(googlePage.isAt());

        googlePage.getSearchComponent().search("Spring boot");
        Assert.assertTrue(googlePage.getSearchResults().isAt());
        Assert.assertTrue(googlePage.getSearchResults().getCount() > 2);
        try {
            screenshotUtil.takeScreenshot();
        } catch (IOException e) {
            e.printStackTrace();
        }
        googlePage.close();
    }
}
