package com.ssandeep79.springseleniumdemo;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "classpath:features",
    tags = "@visa",
    glue = {"com.ssandeep79.springseleniumdemo.bdd"},
    plugin = {
        "pretty",
        "summary",
        "html:target/cucumber.html",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
    monochrome = true
)
public class CucumberRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios () {
        return super.scenarios();
    }
}
