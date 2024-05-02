package com.ssandeep79.springseleniumdemo.page.google;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

import com.ssandeep79.springseleniumdemo.page.Base;
@Component
public class SearchResults extends Base {
    @FindBy(css = "div.rc")
    private List<WebElement> results;

    @Override
    public boolean isAt() {
        return wait.until((d) -> !results.isEmpty());
    }

    public int getCount(){
        return results.size();
    }
}