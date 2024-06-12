package com.ssandeep79.springseleniumdemo.flights;

import com.ssandeep79.springseleniumdemo.SpringBaseTestNGTest;
import com.ssandeep79.springseleniumdemo.model.flights.FlightLangDetails;
import com.ssandeep79.springseleniumdemo.page.flights.FlightsPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FlightTest extends SpringBaseTestNGTest {

    @Autowired
    private FlightLangDetails flightLangDetails;
    @Autowired
    private FlightsPage flightsPage;

    @Test
    public void testFlightPageLabels () {
        flightsPage.goTo(flightLangDetails.getUrl());
        Assert.assertTrue(flightsPage.isAt());
        var categories = flightsPage.getCategories();
        var expectedCategories = flightLangDetails.getCategories();
        Assert.assertEquals(categories.size(), expectedCategories.size());
        Assert.assertEquals(categories, expectedCategories);
    }
}