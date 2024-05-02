package com.ssandeep79.springseleniumdemo.page.google;

import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.ssandeep79.springseleniumdemo.annotation.PageFragment;
import com.ssandeep79.springseleniumdemo.page.Base;

@PageFragment
public class SearchComponent extends Base {

    @FindBy(name = "q")
    private WebElement searchBox;
    @FindBy(name = "btnK")
    private List<WebElement> searchBtns;

    @Override
    public boolean isAt() {
        return wait.until(d -> searchBox.isDisplayed());
    }

    public void search(final String searchTerm) {
        searchBox.sendKeys(searchTerm);
        searchBox.sendKeys(Keys.TAB);
        searchBtns.stream()
                    .filter(e -> e.isDisplayed() && e.isEnabled())
                    .findFirst()
                    .ifPresent(WebElement::click);
    }
}