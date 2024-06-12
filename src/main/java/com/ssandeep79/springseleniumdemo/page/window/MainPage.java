package com.ssandeep79.springseleniumdemo.page.window;

import com.ssandeep79.springseleniumdemo.demo.annotation.Page;
import com.ssandeep79.springseleniumdemo.page.Base;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Page
public class MainPage extends Base {
    @FindBy (tagName = "a")
    private List<WebElement> links;

    public void goTo() {
        driver.get("https://vins-udemy.s3.amazonaws.com/ds/window/main.html");
    }

    public void launchAllWindows() {
        for(WebElement link : links) {
            link.click();
        }
    }

    @Override
    public boolean isAt () {
        return wait.until(d -> !links.isEmpty());
    }
}
