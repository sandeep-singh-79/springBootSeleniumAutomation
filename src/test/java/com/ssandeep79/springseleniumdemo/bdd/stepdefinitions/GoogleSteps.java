package com.ssandeep79.springseleniumdemo.bdd.stepdefinitions;

import com.ssandeep79.springseleniumdemo.page.google.GooglePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.testng.Assert.assertTrue;

@CucumberContextConfiguration
@SpringBootTest
public class GoogleSteps {
    @Autowired
    private GooglePage googlePage;

    @Given("I am on Google home page")
    public void iAmOnGoogleHomePage () {
        googlePage.goTo();
    }

    @When("I search for {string} keyword")
    public void iSearchForKeyword (String keyword) {
        googlePage.getSearchComponent().search(keyword);
    }

    @Then("I should see at least {int} results")
    public void iShouldSeeAtLeastResults (int count) {
        assertTrue(googlePage.getSearchResults().isAt());
        assertTrue(googlePage.getSearchResults().getCount() >= count);
    }
}
