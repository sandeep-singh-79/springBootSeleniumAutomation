package com.ssandeep79.springseleniumdemo.bdd.stepdefinitions;

import com.ssandeep79.springseleniumdemo.page.visa.VisaRegistrationPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.testng.Assert.assertTrue;

public class VisaSteps {
    @Autowired
    private VisaRegistrationPage registrationPage;

    @Given("I am on the visa registration page")
    public void iAmOnTheVisaRegistrationPage () {
        registrationPage.goTo();
    }

    @And("I submit the form")
    public void iSubmitTheForm () {
        registrationPage.submit();
    }

    @Then("I should see a success message")
    public void iShouldSeeASuccessMessage () {
        var visaConfirmationNumber = registrationPage.getConfirmationNumber();
        assertTrue(StringUtils.isNotEmpty(visaConfirmationNumber));
        assertTrue(StringUtils.isAlphanumeric(visaConfirmationNumber));
    }

    @When("I enter relevant registration details - {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}")
    public void iEnterRelevantRegistrationDetails (String fromCountry, String toCountry, String dob, String firstName, String lastName, String email, String phone, String comments) {
        registrationPage.setCountryFromAndTo(fromCountry, toCountry);
        registrationPage.setBirthDate(LocalDate.parse(dob, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        registrationPage.setName(firstName, lastName);
        registrationPage.setContactDetails(email, phone);
        registrationPage.setComments(comments);
    }
}