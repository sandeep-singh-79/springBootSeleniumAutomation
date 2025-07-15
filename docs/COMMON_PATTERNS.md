# Common Patterns Guide

This guide covers common design patterns and best practices for writing tests in our Spring Boot Selenium framework. For general framework information, refer to the [README.md](../README.md). For specific testing approaches, see the [BDD Testing Guide](BDD_TESTING_GUIDE.md) or [TestNG Guide](TESTNG_GUIDE.md).

## Table of Contents

- [Page Object Pattern](#page-object-pattern)
- [Component-Based Design](#component-based-design)
- [Wait Strategies](#wait-strategies)
- [Screenshot Capture](#screenshot-capture)
- [Custom Annotations](#custom-annotations)
- [Exception Handling](#exception-handling)
- [AOP Features](#aop-features)
- [Resource Management](#resource-management)
- [Framework Integration](#framework-integration)

## Page Object Pattern

The Page Object Pattern is a design pattern that creates an object repository for web UI elements. Our framework implements this pattern with a base class and specialized page objects. This is also an implementation of the Template Pattern, where the abstract `Base` class defines the template structure and subclasses provide specific implementations.

### Base Page Class as a Template

All page objects extend the `Base` class, which serves as a template defining the common structure and behaviors:

```java
public abstract class Base {
    @LazyAutowired
    protected WebDriver driver;

    @LazyAutowired
    protected WebDriverWait wait;

    @PostConstruct
    protected void init() {
        PageFactory.initElements(driver, this);
    }

    public abstract boolean isAt();
}
```

### Template Pattern in Page Objects

The abstract `Base` class implements the Template Pattern by:

1. **Defining the skeleton**: The basic structure every page object must follow
2. **Providing common initialization**: The `init()` method with `@PostConstruct` sets up PageFactory
3. **Declaring abstract methods**: The `isAt()` method that subclasses must implement
4. **Managing shared resources**: Common WebDriver and WebDriverWait instances

This pattern ensures consistency across all page objects while allowing each specific page to define its unique elements and behaviors. The `isAt()` method is particularly important as it represents the "hook" that each concrete page must implement to verify the page is correctly loaded.

### Creating Page Objects

To create a page object:

1. Create a class that extends `Base`
2. Annotate it with `@Page`
3. Add WebElement fields with `@FindBy` annotations
4. Implement the `isAt()` method
5. Add methods for page interactions

Example:

```java
@Page
public class VisaRegistrationPage extends Base {
    @FindBy(id = "first_4")
    private WebElement firstName;

    @FindBy(id = "last_4")
    private WebElement lastName;

    // More elements...

    @Override
    public boolean isAt() {
        return wait.until(driver -> firstName.isDisplayed());
    }

    public void setName(String firstName, String lastName) {
        this.firstName.sendKeys(firstName);
        this.lastName.sendKeys(lastName);
    }

    // More methods...
}
```

## Component-Based Design

Complex pages can be broken down into components for better organization and reuse.

### Page Components

Page components are fragments of a page:

```java
@PageFragment
public class SearchComponent extends Base {
    @FindBy(name = "q")
    private WebElement searchBox;
    
    @FindBy(name = "btnK")
    private List<WebElement> searchBtns;

    @Override
    public boolean isAt() {
        return wait.until(d -> searchBox.isDisplayed());
    }

    public void search(final String searchTerm) {
        searchBox.sendKeys(searchTerm);
        searchBox.sendKeys(Keys.TAB);
        searchBtns.stream()
                .filter(e -> e.isDisplayed() && e.isEnabled())
                .findFirst()
                .ifPresent(WebElement::click);
    }
}
```

### Using Components in Pages

Components are autowired into pages:

```java
@Page
public class GooglePage extends Base {
    @LazyAutowired
    private SearchComponent searchComponent;
    
    @LazyAutowired
    private SearchResults searchResults;

    @Override
    public boolean isAt() {
        return searchComponent.isAt();
    }

    public SearchComponent getSearchComponent() {
        return searchComponent;
    }
    
    // More methods...
}
```

## Wait Strategies

Our framework implements explicit waits using WebDriverWait.

### WebDriverWait Configuration

The wait timeout is configured in `WebDriverWaitConfig`:

```java
@LazyConfiguration
public class WebDriverWaitConfig {
    @Value("${default.timeout:30}")
    private int timeout;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WebDriverWait webdriverWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(this.timeout));
    }
}
```

### Using Waits in Page Objects

The `wait` field is inherited from the `Base` class:

```java
@Override
public boolean isAt() {
    return wait.until(driver -> firstName.isDisplayed());
}

public String getConfirmationNumber() {
    wait.until(driver -> requestNumber.isDisplayed());
    return this.requestNumber.getText();
}
```

## Screenshot Capture

Our framework provides screenshot capture through the `ScreenshotService` and AOP.

### Screenshot Service

The `ScreenshotService` provides methods for taking screenshots:

```java
@Lazy
@Service
public class ScreenshotService {
    @Value("${screenshot.path}")
    private Path screenshotPath;

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private Faker faker;

    public void takeScreenshot(String filename) throws IOException {
        File scrFile = ctx.getBean(TakesScreenshot.class).getScreenshotAs(OutputType.FILE);
        Path outFile = screenshotPath.resolve(filename);
        // Save the screenshot
    }

    public byte[] getScreenshot() {
        return ctx.getBean(TakesScreenshot.class).getScreenshotAs(OutputType.BYTES);
    }
}
```

### Screenshot Aspect

The `ScreenshotAspect` enables automatic screenshots through annotations:

```java
@Aspect
@Slf4j
@Service
public class ScreenshotAspect {
    @Autowired
    private ScreenshotService screenshotService;

    @After("@annotation(takeScreenshot)")
    public void after(TakeScreenshot takeScreenshot) throws IOException {
        screenshotService.takeScreenshot();
    }
}
```

### Using Screenshots in Tests

Use the `@TakeScreenshot` annotation to capture screenshots:

```java
@Test
@TakeScreenshot
public void testWithScreenshot() {
    // Test code that will have screenshot taken after completion
}
```

## Custom Annotations

Our framework uses custom annotations for various purposes:

### @Page and @PageFragment

For page objects and components:

```java
@Page
public class GooglePage extends Base {
    // Page implementation
}

@PageFragment
public class SearchComponent extends Base {
    // Component implementation
}
```

### @LazyAutowired and @LazyConfiguration

For lazy initialization of beans:

```java
@LazyAutowired
private SearchComponent searchComponent;

@LazyConfiguration
public class WebDriverConfig {
    // Configuration code
}
```

### @ThreadScopeBean

For thread-scoped beans (especially WebDriver):

```java
@ThreadScopeBean
@ConditionalOnProperty(name = "browser", havingValue = "chrome", matchIfMissing = true)
public WebDriver chromeDriver() {
    // WebDriver initialization
}
```

### @Window

For window management:

```java
@Window("Page A")
@Page
public class PageA extends Base {
    // Page implementation
}
```

## Exception Handling

Our framework handles exceptions in several ways:

### Try-Catch Blocks

Use try-catch blocks for expected exceptions:

```java
try {
    screenshotService.takeScreenshot();
} catch (IOException e) {
    e.printStackTrace();
}
```

### Expected Exceptions in TestNG

Specify expected exceptions in TestNG:

```java
@Test(expectedExceptions = ElementNotInteractableException.class)
public void testElementNotInteractable() {
    // Test that should throw the expected exception
}
```

## AOP Features

Our framework uses Aspect-Oriented Programming for cross-cutting concerns:

### Window Aspect

Automatically switches windows based on annotations:

```java
@Aspect
@Service
public class WindowAspect {
    @Autowired
    private WindowSwitchService switchService;

    @Before("@target(window) && within(com.ssandeep79.springseleniumdemo..*)")
    public void before(Window window) {
        switchService.switchByTitle(window.value());
    }

    @After("@target(window) && within(com.ssandeep79.springseleniumdemo..*)")
    public void after(Window window) {
        switchService.switchByIndex(0);
    }
}
```

### Screenshot Aspect Implementation

Captures screenshots based on annotations:

```java
@Aspect
@Slf4j
@Service
public class ScreenshotAspect {
    @Autowired
    private ScreenshotService screenshotService;

    @After("@annotation(takeScreenshot)")
    public void after(TakeScreenshot takeScreenshot) throws IOException {
        screenshotService.takeScreenshot();
    }
}
```

## Resource Management

Our framework manages resources to prevent memory leaks and ensure proper cleanup:

### WebDriver Lifecycle

WebDrivers are automatically closed through shutdown hooks:

```java
@ThreadScopeBean
@ConditionalOnProperty(name = "browser", havingValue = "chrome", matchIfMissing = true)
public WebDriver chromeDriver() {
    WebDriverManager.chromedriver().setup();
    ChromeOptions options = new ChromeOptions();
    // Configure options
    
    var chromeDriver = new ChromeDriver(options);
    Runtime.getRuntime().addShutdownHook(new Thread(chromeDriver::quit));
    return chromeDriver;
}
```

### Browser Scope

A custom scope manages WebDriver instances:

```java
public class BrowserScope extends ThreadScope {
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Object o = super.get(name, objectFactory);
        SessionId sessionId = ((RemoteWebDriver)o).getSessionId();
        if (Objects.isNull(sessionId)) {
            super.remove(name);
            o = super.get(name, objectFactory);
        }
        return o;
    }
}
```

### Cucumber Hooks

Cucumber hooks clean up resources after scenarios:

```java
@After
public void afterScenario(Scenario scenario) {
    log.info("Scenario '{}' - {}", scenario.getName(), scenario.getStatus());
    var webDriver = ctx.getBean(WebDriver.class);
    if (webDriver != null) {
        log.info("Closing the browser");
        webDriver.manage().deleteAllCookies();
    }
}
```

## Framework Integration

Our framework uniquely integrates BDD and TestNG approaches, allowing you to leverage the strengths of both.

### BDD and TestNG Integration

The integration happens through the `CucumberRunner` class, which extends `AbstractTestNGCucumberTests`:

```java
public class CucumberRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
```

This allows:

- Running Cucumber features through TestNG
- Parallel execution of scenarios
- Using TestNG's reporting capabilities
- Integrating with CI/CD tools that support TestNG

### Shared Context

Both BDD and TestNG tests use the same Spring application context, which means:

1. They share the same dependency injection configuration
2. They can use the same page objects and components
3. Common aspects (like screenshot capture) work across both approaches

### When to Use Each Approach

- **BDD (Cucumber)**: Best for user-facing features that benefit from collaboration with business stakeholders
- **TestNG**: Better for technical tests, API tests, and complex test logic

For guidance on implementing each approach, see the [BDD Testing Guide](BDD_TESTING_GUIDE.md) or [TestNG Guide](TESTNG_GUIDE.md)
