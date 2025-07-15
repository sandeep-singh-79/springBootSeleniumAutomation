# TestNG Testing Guide

This guide explains how to create and maintain tests using TestNG with our Spring Boot Selenium framework. For general framework information, refer to the [README.md](../README.md), and for information about common patterns used in the framework, see the [Common Patterns Guide](COMMON_PATTERNS.md).

## Table of Contents

- [Introduction to TestNG](#introduction-to-testng)
- [Setting Up a TestNG Test](#setting-up-a-testng-test)
- [Test Structure](#test-structure)
- [Data-Driven Testing](#data-driven-testing)
- [Test Configuration](#test-configuration)
- [Parallel Execution](#parallel-execution)
- [Best Practices](#best-practices)
- [Advanced Usage](#advanced-usage)

## Introduction to TestNG

TestNG is a testing framework designed to simplify a broad range of testing needs, from unit testing to integration testing. Our framework integrates TestNG with Spring Boot for dependency injection and configuration management.

Key TestNG components in our framework:

- **SpringBaseTestNGTest**: Base class for all TestNG tests
- **TestNG Suite XML**: Test suite configuration
- **DataProviders**: For data-driven testing
- **Test Listeners**: For test execution events

## Setting Up a TestNG Test

1. Create a test class that extends `SpringBaseTestNGTest`
2. Use `@Autowired` to inject page objects and other dependencies
3. Add TestNG annotations (`@Test`, `@BeforeMethod`, etc.) to your test methods

### Running TestNG Tests

To run TestNG tests using the default suite:

```bash
mvn test -DsuiteXmlFile=testng-suite.xml
```

To run a specific test class:

```bash
mvn test -Dtest=YourTestClassName
```

To run a specific test method:

```bash
mvn test -Dtest=YourTestClassName#methodName
```

Example test class:

```java
public class FlightTest extends SpringBaseTestNGTest {

    @Autowired
    private FlightLangDetails flightLangDetails;
    @Autowired
    private FlightsPage flightsPage;

    @Test
    public void testFlightPageLabels() {
        flightsPage.goTo(flightLangDetails.getUrl());
        Assert.assertTrue(flightsPage.isAt());
        var categories = flightsPage.getCategories();
        var expectedCategories = flightLangDetails.getCategories();
        Assert.assertEquals(categories.size(), expectedCategories.size());
        Assert.assertEquals(categories, expectedCategories);
    }
}
```

## Test Structure

### Test Class Organization

Test classes should be organized by functionality or feature. Each test class should:

1. Extend `SpringBaseTestNGTest` to inherit Spring Boot integration
2. Use Spring's `@Autowired` to inject dependencies
3. Have clear, focused test methods
4. Use meaningful assertions

### Test Methods

Each test method should:

1. Have a clear purpose described by its name
2. Follow the Arrange-Act-Assert pattern
3. Have proper assertions
4. Be independent of other test methods

Example:

```java
@Test
public void testUserVisaRecordSize() {
    // Arrange - implicit from autowiring
    
    // Act & Assert
    assertEquals(customerRepository.findAll().size(), 100);
}

@Test
public void testForSpecificUserVisaRecord() {
    // Arrange - implicit from autowiring
    
    // Act & Assert
    assertTrue(customerRepository.findById(85).isPresent());
    assertEquals(customerRepository.findById(85).get().getFirstName(), "Andrea");
}
```

### Test Lifecycle Methods

TestNG provides annotations to execute code at different phases of the test lifecycle:

- `@BeforeClass`: Run before any test method in the class
- `@BeforeMethod`: Run before each test method
- `@AfterMethod`: Run after each test method
- `@AfterClass`: Run after all test methods in the class

Example:

```java
@BeforeClass
public void setup() {
    mainPage.goTo();
    mainPage.isAt();
    mainPage.launchAllWindows();
}

@AfterClass
public void tearDown() {
    // Clean up resources
}
```

## Data-Driven Testing

TestNG provides powerful data-driven testing capabilities through DataProviders. Our framework enhances this with Spring's dependency injection.

### Using DataProviders

```java
@Test(dataProvider = "visaData")
public void visaTest(VisaDetails details) {
    // Test implementation using the data
}

@DataProvider(name = "visaData")
public Object[][] visaData() {
    return new Object[][] {
        {new VisaDetails("Isle of Man", "Mali", "2011-05-31", "Kraig", "Wiza", "Kraig@nobody.com", "1-000-884-13", "")},
        {new VisaDetails("Lithuania", "Mexico", "2001-01-01", "Houston", "Kertzmann", "Houston@nobody.com", "284.864.6580", "")}
    };
}
```

### Data Repositories

For more complex data management, our framework uses Spring Data repositories:

```java
public interface VisaDetailsRepository extends JpaRepository<VisaDetails, Integer> {
    @Query("FROM VisaDetails WHERE id = :id")
    VisaDetails getById(int id);
}
```

You can then inject the repository into your test:

```java
@Autowired
private VisaDetailsRepository visaRepo;

@Test
public void testWithRepositoryData() {
    VisaDetails details = visaRepo.getById(1);
    // Test using the data
}
```

### External Test Data

For tests that use external data sources (CSV, Excel, JSON):

1. Place data files in `src/test/resources/data/`
2. Create a data reader class that loads the data
3. Use the data reader in your DataProvider

Example:

```java
@Component
public class CsvDataReader {
    public List<VisaDetails> readVisaDetails(String filename) {
        // Implementation that reads CSV file
    }
}

@Autowired
private CsvDataReader dataReader;

@DataProvider(name = "csvVisaData")
public Object[][] csvVisaData() {
    List<VisaDetails> details = dataReader.readVisaDetails("visa_test_data.csv");
    return details.stream()
                 .map(d -> new Object[]{d})
                 .toArray(Object[][]::new);
}
```

## Test Configuration

### TestNG Suite XML

The TestNG suite XML file configures test execution:

```xml
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="spring-boot" parallel="tests" thread-count="2">
    <parameter name="dobFrom" value="1995-01-01" />
    <parameter name="dobTo" value="1997-01-01" />
    <test name="visaDataProviderParameterization">
        <classes>
            <class name="com.ssandeep79.springseleniumdemo.visa.UserVisaTest" />
        </classes>
    </test>
    <!-- Add more test classes here -->
</suite>
```

### Spring Properties

Tests can be configured via Spring properties:

- `application.properties`: Default properties
- `application-qa.properties`: QA-specific properties
- `application-remote.properties`: Remote WebDriver properties

Properties can be injected into tests:

```java
@Value("${application.url}")
private String applicationUrl;
```

## Parallel Execution

Our framework supports parallel test execution through:

1. TestNG parallel configuration
2. Thread-safe WebDriver management
3. Browser scope for WebDriver instances

### TestNG Parallel Configuration

In testng-suite.xml:

```xml
<suite name="spring-boot" parallel="tests" thread-count="2">
    <!-- Test classes -->
</suite>
```

### Thread-Safe DataProviders

For data-driven parallel execution:

```java
@DataProvider(parallel = true)
public Object[][] scenarios() {
    return super.scenarios();
}
```

## Best Practices

1. **Independent Tests**: Each test should be independent of others
2. **Clear Test Names**: Use descriptive test method names
3. **Proper Assertions**: Use meaningful assertions with error messages
4. **Page Objects**: Encapsulate UI interactions in page objects
5. **Clean Resources**: Clean up resources in @AfterMethod or @AfterClass
6. **Avoid Thread Issues**: Ensure test data is thread-safe for parallel execution
7. **Targeted Tests**: Keep tests focused on a specific functionality
8. **Screenshots on Failure**: Use AOP to capture screenshots on test failures

## Advanced Usage

### Conditional Test Execution

Use TestNG's conditional execution features:

```java
@Test(dependsOnMethods = {"testLogin"})
public void testDashboard() {
    // This test runs only if testLogin passes
}

@Test(groups = {"smoke"})
public void testBasicFunctionality() {
    // This test belongs to the "smoke" group
}
```

### Test Listeners

Create custom TestNG listeners:

```java
public class CustomTestListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        // Take screenshot or other actions on test failure
    }
    
    // Implement other methods as needed
}
```

Register the listener in testng-suite.xml:

```xml
<suite name="spring-boot">
    <listeners>
        <listener class-name="com.ssandeep79.springseleniumdemo.listeners.CustomTestListener" />
    </listeners>
    <!-- Tests -->
</suite>
```

### Factory Pattern for Dynamic Tests

Use TestNG's factory pattern for dynamic test creation:

```java
@Factory
public Object[] createTests() {
    List<Object> tests = new ArrayList<>();
    for (String browser : Arrays.asList("chrome", "firefox")) {
        tests.add(new BrowserSpecificTest(browser));
    }
    return tests.toArray();
}
```
