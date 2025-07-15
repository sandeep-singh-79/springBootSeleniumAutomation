# BDD Testing Guide

This guide explains how to create and maintain BDD tests using Cucumber with our Spring Boot Selenium framework. For general framework information, refer to the [README.md](../README.md), and for information about common patterns used in the framework, see the [Common Patterns Guide](COMMON_PATTERNS.md).

## Table of Contents

- [Introduction to BDD](#introduction-to-bdd)
- [Setting Up a BDD Test](#setting-up-a-bdd-test)
- [Writing Feature Files](#writing-feature-files)
- [Creating Step Definitions](#creating-step-definitions)
- [Managing Test Hooks](#managing-test-hooks)
- [Best Practices](#best-practices)
- [Advanced Usage](#advanced-usage)

## Introduction to BDD

Behavior-Driven Development (BDD) is an agile software development process that encourages collaboration among developers, QA, and business participants. Our framework uses Cucumber to implement BDD tests.

Key BDD components in our framework:

- **Feature files**: Written in Gherkin syntax
- **Step definitions**: Java code that implements the steps
- **CucumberRunner**: TestNG-based runner for Cucumber tests
- **Hooks**: Setup and teardown operations for scenarios

## Setting Up a BDD Test

1. Create a feature file under `src/test/resources/features/`
2. Create step definitions under `src/test/java/com/ssandeep79/springseleniumdemo/bdd/stepdefinitions/`
3. Ensure the `CucumberRunner` includes your feature or tags

The `CucumberRunner` class is configured to run feature files with specific tags:

```java
@CucumberOptions(
    features = "classpath:features",
    tags = "@visa",  // Change this to include your feature tags
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
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
```

### Running BDD Tests

To run your BDD tests, use the Maven command:

```bash
mvn test
```

To run tests with specific tags:

```bash
mvn test -Dcucumber.filter.tags="@your-tag"
```

To generate reports after test execution:

```bash
mvn test -Dcucumber.filter.tags="@your-tag" -Dcucumber.publish.enabled=true
```

## Writing Feature Files

Feature files use Gherkin syntax and should be organized by functional area. Here's an example:

```gherkin
@visa
Feature: Visa Registration

  Scenario Outline: Submit visa registration form
    Given I am on the visa registration page
    When I enter relevant registration details - "<fromCountry>", "<toCountry>", "<dob>", "<first_name>", "<last_name>", "<email>", "<phone>", "<comments>"
    And I submit the form
    Then I should see a success message

    Examples:
      | fromCountry | toCountry | dob        | first_name | last_name | email             | phone        | comments |
      | Isle of Man | Mali      | 2011-05-31 | Kraig      | Wiza      | Kraig@nobody.com  | 1-000-884-13 |          |
      | Lithuania   | Mexico    | 2001-01-01 | Houston    | Kertzmann | Houston@nobody.com| 284.864.6580 |          |
```

### Feature File Best Practices

1. Use clear, descriptive feature and scenario names
2. Tag features and scenarios for better organization
3. Use scenario outlines for data-driven tests
4. Keep steps simple and reusable
5. Organize feature files by business domain

## Creating Step Definitions

Step definitions connect Gherkin steps to Java code. Here's how to create them:

```java
public class VisaSteps {
    @Autowired
    private VisaRegistrationPage registrationPage;

    @Given("I am on the visa registration page")
    public void iAmOnTheVisaRegistrationPage() {
        registrationPage.goTo();
    }

    @And("I submit the form")
    public void iSubmitTheForm() {
        registrationPage.submit();
    }

    @Then("I should see a success message")
    public void iShouldSeeASuccessMessage() {
        var visaConfirmationNumber = registrationPage.getConfirmationNumber();
        assertTrue(StringUtils.isNotEmpty(visaConfirmationNumber));
        assertTrue(StringUtils.isAlphanumeric(visaConfirmationNumber));
    }
    
    @When("I enter relevant registration details - {string}, {string}, {string}, {string}, {string}, {string}, {string}, {string}")
    public void iEnterRelevantRegistrationDetails(String fromCountry, String toCountry, 
                                               String dob, String firstName, String lastName,
                                               String email, String phone, String comments) {
        // Implementation
    }
}
```

### Step Definition Best Practices

1. Keep step definitions focused on a single responsibility
2. Use Spring's `@Autowired` to inject page objects and other dependencies
3. Use clear, descriptive method names
4. Reuse steps where possible
5. Handle exceptions appropriately

## Managing Test Hooks

Hooks allow you to run code before and after scenarios. Our framework provides `CucumberHooks` class:

```java
public class CucumberHooks {
    @Autowired
    private ScreenshotService screenshotService;
    @Autowired
    private ApplicationContext ctx;

    @AfterStep
    public void afterStep(Scenario scenario) {
        if (scenario.isFailed()) {
            scenario.attach(screenshotService.getScreenshot(), "image/png", scenario.getName());
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        // Clean up operations
    }
}
```

### Custom Hooks

You can create additional hooks for specific features using tags:

```java
@After("@cleanDatabase")
public void cleanDatabase() {
    // Database cleanup code
}
```

## Best Practices

1. **Keep Scenarios Independent**: Each scenario should be able to run independently
2. **Use Page Objects**: Encapsulate UI interactions in page objects
3. **Avoid Hardcoded Data**: Use examples tables or external test data
4. **Tag Management**: Organize tests with tags for easier filtering
5. **Clear Assertions**: Use clear assertions with meaningful error messages
6. **Parallel Execution**: Configure scenarios to run in parallel for faster execution
7. **Screenshot on Failure**: Capture screenshots on test failures

## Advanced Usage

### Cucumber Expressions

Use Cucumber expressions for more flexibility in step parameters:

```java
@Given("I have {int} items in my cart")
public void iHaveItemsInMyCart(int itemCount) {
    // Implementation
}
```

### DataTables

Use DataTables for complex data structures:

```java
@When("I add the following items to cart")
public void iAddTheFollowingItemsToCart(DataTable dataTable) {
    List<Map<String, String>> items = dataTable.asMaps();
    // Process items
}
```

### Sharing State Between Steps

Use Spring's dependency injection to share state:

```java
@Component
@Scope("cucumber-glue")
public class TestContext {
    private Map<String, Object> testData = new HashMap<>();
    
    public void set(String key, Object value) {
        testData.put(key, value);
    }
    
    public Object get(String key) {
        return testData.get(key);
    }
}
```

Then inject it in your step definitions:

```java
@Autowired
private TestContext testContext;
```

#### Example Implementation

Here's a complete example of how to use this pattern in your step definitions:

```java
// First step definition class
public class SearchSteps {
    @Autowired
    private GooglePage googlePage;
    
    @Autowired
    private TestContext testContext;
    
    @Given("I search for {string}")
    public void iSearchFor(String searchTerm) {
        googlePage.goTo();
        googlePage.getSearchComponent().search(searchTerm);
        
        // Store the search term in the shared context
        testContext.set("searchTerm", searchTerm);
        
        // Store search results count
        int resultsCount = googlePage.getSearchResults().getCount();
        testContext.set("resultsCount", resultsCount);
    }
}

// Second step definition class
public class ResultsSteps {
    @Autowired
    private SearchResultsPage resultsPage;
    
    @Autowired
    private TestContext testContext;
    
    @Then("I should see results related to my search")
    public void iShouldSeeResultsRelatedToMySearch() {
        // Retrieve the search term from the shared context
        String searchTerm = (String) testContext.get("searchTerm");
        
        // Verify that results contain the search term
        assertTrue(resultsPage.getResults().stream()
            .anyMatch(result -> result.getText().contains(searchTerm)));
        
        // Get and use the results count from previous step
        int resultsCount = (int) testContext.get("resultsCount");
        assertTrue(resultsCount > 0, "Expected to have search results");
    }
}
```

This pattern allows you to:

1. Share data between different step definition classes
2. Pass information from one step to another
3. Store intermediate test results for later verification
4. Maintain state throughout a scenario's execution

The `cucumber-glue` scope ensures that the context is maintained for the duration of a single scenario execution but doesn't leak between different scenarios.
