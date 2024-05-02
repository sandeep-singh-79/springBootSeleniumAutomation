package com.ssandeep79.springseleniumdemo.googletest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ssandeep79.springseleniumdemo.SpringBaseTestNGTest;
import com.ssandeep79.springseleniumdemo.page.google.GooglePage;
import com.ssandeep79.springseleniumdemo.util.ScreenshotUtil;

public class Google1Test extends SpringBaseTestNGTest {

    @Lazy
    @Autowired
    private GooglePage googlePage;
    @Lazy
    @Autowired
    private ScreenshotUtil screenshotUtil;

    @Test
    public void googleTest() {
        googlePage.goTo();
        Assert.assertTrue(googlePage.isAt());

        googlePage.getSearchComponent().search("Spring boot");
        Assert.assertTrue(googlePage.getSearchResults().isAt());
        Assert.assertTrue(googlePage.getSearchResults().getCount() > 2);
        try {
            screenshotUtil.takeScreenshot(String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.of(ZoneOffset.SHORT_IDS.get("IST")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
