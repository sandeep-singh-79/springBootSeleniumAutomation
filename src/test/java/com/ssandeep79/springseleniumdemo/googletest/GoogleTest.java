package com.ssandeep79.springseleniumdemo.googletest;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ssandeep79.springseleniumdemo.SpringBaseTestNGTest;
import com.ssandeep79.springseleniumdemo.page.google.GooglePage;

public class GoogleTest extends SpringBaseTestNGTest {

    @Autowired
    private GooglePage googlePage;

    @Test
    public void googleTest() {
        googlePage.goTo();
        Assert.assertTrue(googlePage.isAt());

        googlePage.getSearchComponent().search("Spring boot");
        Assert.assertTrue(googlePage.getSearchResults().isAt());
        Assert.assertTrue(googlePage.getSearchResults().getCount() > 2);
    }

}
