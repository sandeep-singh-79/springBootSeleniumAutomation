package com.ssandeep79.springseleniumdemo;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "classpath:features",
    tags = "@visa or @google",
    glue = {"classpath:com.ssandeep79.springseleniumdemo.bdd",
        "classpath:com.ssandeep79.springseleniumdemo.bdd.stepdefinitions"},
    plugin = {
        "pretty",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
        "summary"},
    monochrome = true
)
public class CucumberRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios () {
        return super.scenarios();
    }
}
