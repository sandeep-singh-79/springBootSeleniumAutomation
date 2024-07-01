package com.ssandeep79.springseleniumdemo;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.ssandeep79.springseleniumdemo.bdd.stepdefinitions"/*,
        plugin = {"pretty", "html:target/cucumber-reports", "json:target/cucumber-reports/cucumber.json"}*/
)
public class CucumberRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
