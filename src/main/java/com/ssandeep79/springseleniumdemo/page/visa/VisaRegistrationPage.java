package com.ssandeep79.springseleniumdemo.page.visa;

import com.ssandeep79.springseleniumdemo.demo.annotation.Page;
import com.ssandeep79.springseleniumdemo.page.Base;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Page
public class VisaRegistrationPage extends Base {
    private static final Logger logger = LoggerFactory.getLogger(VisaRegistrationPage.class);

    @FindBy(id = "first_4")
    private WebElement firstName;

    @FindBy(id = "last_4")
    private WebElement lastName;

    @FindBy(id = "input_46")
    private WebElement fromCountry;

    @FindBy(id = "input_47")
    private WebElement toCountry;

    @FindBy(id = "input_24_month")
    private WebElement month;

    @FindBy(id = "input_24_day")
    private WebElement day;

    @FindBy(id = "input_24_year")
    private WebElement year;

    @FindBy(id = "input_6")
    private WebElement email;

    @FindBy(id = "input_27_area")
    private WebElement areaCode;

    @FindBy(id = "input_27_phone")
    private WebElement phone;

    @FindBy(id = "input_45")
    private WebElement comments;

    @FindBy(id = "submitBtn")
    private WebElement submit;

    @FindBy(id = "requestnumber")
    private WebElement requestNumber;

    public void goTo () {
        this.driver.get("https://vins-udemy.s3.amazonaws.com/sb/visa/udemy-visa.html");
    }

    @Override
    public boolean isAt () {
        return wait.until(driver -> firstName.isDisplayed());
    }

    public void setName (String firstName, String lastName) {
        this.firstName.sendKeys(firstName);
        this.lastName.sendKeys(lastName);
    }

    public void setCountryFromAndTo (String fromCountry, String toCountry) {
        new Select(this.fromCountry).selectByValue(fromCountry);
        new Select(this.toCountry).selectByValue(toCountry);
    }

    public void setBirthDate (LocalDate localDate) {
        new Select(year).selectByVisibleText(String.valueOf(localDate.getYear()));
        new Select(month).selectByValue(String.valueOf(localDate.getMonth()));
        new Select(day).selectByVisibleText(String.valueOf(localDate.getDayOfMonth()));
    }

    public void setContactDetails (String email, String phoneNumber) {
        this.email.sendKeys(email);

        Pattern phonePattern = Pattern.compile("^(\\d{1,3}-)?(\\d{3})-(\\d{3}-\\d{4})$");
        Matcher phoneMatcher = phonePattern.matcher(phoneNumber);

        if (phoneMatcher.matches()) {
            this.areaCode.sendKeys(phoneMatcher.group(2));
            this.phone.sendKeys(phoneMatcher.group(3));
        }
    }

    public void setComments (String comments) {
        this.comments.sendKeys(Objects.toString(comments, ""));
    }

    public void submit () {
        this.submit.click();
    }

    public String getConfirmationNumber () {
        wait.until(driver -> requestNumber.isDisplayed());
        return this.requestNumber.getText();
    }
}
