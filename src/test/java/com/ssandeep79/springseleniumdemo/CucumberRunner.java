package com.ssandeep79.springseleniumdemo;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.ssandeep79.springseleniumdemo.bdd.stepdefinitions"/*,
        plugin = {"pretty", "html:target/cucumber-reports", "json:target/cucumber-reports/cucumber.json"}*/
)
public class CucumberRunner extends AbstractTestNGCucumberTests {
}
