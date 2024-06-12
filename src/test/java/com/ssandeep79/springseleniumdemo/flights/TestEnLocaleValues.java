package com.ssandeep79.springseleniumdemo.flights;

import org.springframework.test.context.TestPropertySource;

@TestPropertySource (properties = "app.locale=en")
public class TestEnLocaleValues extends FlightTest {
}
