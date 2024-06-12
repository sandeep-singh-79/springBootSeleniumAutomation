package com.ssandeep79.springseleniumdemo.page.flights;

import com.ssandeep79.springseleniumdemo.demo.annotation.Page;
import com.ssandeep79.springseleniumdemo.page.Base;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Page
public class FlightsPage extends Base {

    @FindBy(css = "nav.P6Wwdb.OE019d button")
    private List<WebElement> categories;

    @Override
    public boolean isAt () {
        return wait.until(d -> !categories.isEmpty());
    }

    public void goTo(final String url) {
        driver.get(url);
        driver.manage().window().maximize();
    }

    // method to get all labels of the categories
    public List<String> getCategories() {
        return categories.stream().map(WebElement::getText).map(String::trim).toList();
    }
}
