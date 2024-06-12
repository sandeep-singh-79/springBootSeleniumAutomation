package com.ssandeep79.springseleniumdemo.flights;

import org.springframework.test.context.TestPropertySource;

@TestPropertySource (properties = {"app.locale=es", "browser=firefox"})
public class TestEsLocaleValues extends FlightTest {
}
