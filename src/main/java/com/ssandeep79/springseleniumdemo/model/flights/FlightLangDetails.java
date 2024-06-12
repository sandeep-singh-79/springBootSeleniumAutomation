package com.ssandeep79.springseleniumdemo.model.flights;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PropertySource("language/${app.locale}.properties")
public class FlightLangDetails {
    @Value("${flight.app.url}")
    private String url;

    @Value("${flight.app.labels}")
    private List<String> categories;

    public String getUrl () {
        return url;
    }

    public List<String> getCategories () {
        return categories;
    }
}
