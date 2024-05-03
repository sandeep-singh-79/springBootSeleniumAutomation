package com.ssandeep79.springseleniumdemo.page.google;

import com.ssandeep79.springseleniumdemo.annotation.PageFragment;
import com.ssandeep79.springseleniumdemo.page.Base;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@PageFragment
public class SearchResults extends Base {
    @FindBy(xpath = "//*[@id='search']//div[@jscontroller]")
    private List<WebElement> results;

    @Override
    public boolean isAt() {
        return wait.until(d -> !results.isEmpty());
    }

    public int getCount(){
        return results.size();
    }
}