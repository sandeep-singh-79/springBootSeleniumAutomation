package com.ssandeep79.springseleniumdemo.page.window;

import com.ssandeep79.springseleniumdemo.demo.annotation.Window;
import com.ssandeep79.springseleniumdemo.page.Base;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Window("Page A")
public class PageA extends Base {
    @FindBy (id = "area")
    private WebElement textArea;

    public void goTo() {
        driver.get("https://vins-udemy.s3.amazonaws.com/ds/window/main.html");
    }

    public void addToArea (String text) {
        textArea.sendKeys(text);
    }

    @Override
    public boolean isAt () {
        return wait.until(d -> textArea.isDisplayed());
    }
}
