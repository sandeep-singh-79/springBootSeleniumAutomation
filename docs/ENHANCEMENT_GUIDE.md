# Framework Enhancement Guide

This document provides an overview of the enhancements made to the Spring Boot Selenium Automation Framework, detailing new features and explaining how to use them.

## Table of Contents

1. [Utility Classes](#1-utility-classes)
2. [Enhanced Error Handling](#2-enhanced-error-handling)
3. [Test Data Management](#3-test-data-management)
4. [CI/CD Support](#4-cicd-support)
5. [API Testing Framework](#5-api-testing-framework)

## 1. Utility Classes

### WebElementUtility

This utility class provides advanced WebElement interactions beyond basic Selenium capabilities.

**Key Features:**
- Enhanced wait mechanisms with retry capabilities
- Advanced element interactions (drag-and-drop, hover)
- JavaScript execution helpers
- Shadow DOM handling
- Element visibility and state checking

**Usage Example:**

```java
@Autowired
private WebElementUtility elementUtil;

// Perform an action with retry mechanism
elementUtil.clickWithRetry(submitButton, 3);

// Scroll element into view and highlight
elementUtil.scrollIntoView(element);
elementUtil.highlightElement(element, 500);

// Work with shadow DOM
WebElement shadowContent = elementUtil.findElementInShadowDOM(hostElement, "div.content");
```

### DateTimeUtility

This utility provides comprehensive date and time manipulation features.

**Key Features:**
- Date formatting with locale support
- Timezone conversion
- Date arithmetic (add days, business days)
- Date comparison and validation
- Human-readable duration formatting

**Usage Example:**

```java
// Format dates
String formattedDate = DateTimeUtility.formatDate(LocalDate.now(), "dd-MMM-yyyy", Locale.US);

// Get dates in different timezones
LocalDateTime dateTimeInTokyo = DateTimeUtility.getCurrentDateTimeInTimezone("Asia/Tokyo");

// Add business days (skipping weekends)
LocalDate deadline = DateTimeUtility.addBusinessDays(LocalDate.now(), 5);
```

### FileUtility

This utility handles file operations for testing.

**Key Features:**
- File download verification
- File comparison
- File checksum calculation
- File upload helpers
- Temporary file management

**Usage Example:**

```java
@Autowired
private FileUtility fileUtil;

// Wait for download
File downloadedFile = fileUtil.waitForFileDownload(
    "C:/Downloads", "report-.*\\.pdf", 30);

// Compare files
boolean filesMatch = FileUtility.compareFiles(expectedFile, actualFile);

// Calculate checksum
String md5 = FileUtility.getFileMD5Checksum(file);
```

### RandomDataUtility

This utility generates various types of test data.

**Key Features:**
- Extended Faker integration
- Custom data generators
- Domain-specific data
- Configurable randomization

**Usage Example:**

```java
@Autowired
private RandomDataUtility randomData;

// Generate test data
String email = randomData.randomEmail(true, true);
String phone = randomData.randomPhoneNumber("+1", true);
String password = randomData.randomPassword(12, true, true, true);
```

### BrowserUtility

This utility provides browser-specific operations.

**Key Features:**
- Cookie management
- Local/session storage access
- Console log capture
- Network traffic monitoring
- Browser metrics collection

**Usage Example:**

```java
@Autowired
private BrowserUtility browserUtil;

// Initialize DevTools
browserUtil.initDevTools();

// Start monitoring network
browserUtil.startNetworkMonitoring();

// Work with cookies and storage
browserUtil.addCookie("session", "abc123", "/", null, null, true);
browserUtil.setLocalStorageItem("theme", "dark");

// Get console errors
List<LogEntry> errors = browserUtil.getConsoleErrors();

// Get performance metrics
Map<String, Object> metrics = browserUtil.getPerformanceMetrics();
```

## 2. Enhanced Error Handling

### RetryAnalyzer

Provides automatic retry capability for flaky tests.

**Key Features:**
- TestNG integration for automatic retries
- Configurable retry counts and delays
- Method-level control via annotations
- Supports conditional retry based on exception types

**Usage Example:**

```java
// Mark test method for retry
@Test
@RetryTest(maxRetryCount = 3, delayBetweenRetriesMs = 2000)
public void flakeyTest() {
    // Test that might be flaky
}

// Retry only for specific exceptions
@Test
@RetryTest(retryFor = {StaleElementReferenceException.class, TimeoutException.class})
public void testWithPotentialStaleElements() {
    // Test with potential stale elements
}
```

### Custom Exception Framework

Provides a structured approach to exception handling.

**Key Features:**
- Categorized exceptions by type
- Enhanced error messages with context
- Factory methods for common exceptions
- Consistent exception hierarchy

**Usage Example:**

```java
// Use exception factory methods
throw FrameworkException.elementNotFound("button#submit");
throw FrameworkException.timeout("Page load", 30);
throw FrameworkException.validationError("Expected count: 5, Actual: 3");
```

## 3. Test Data Management

### ExcelDataProvider

Reads test data from Excel files.

**Key Features:**
- TestNG data provider integration
- Sheet and column filtering
- Header mapping
- Excel file writing capabilities

**Usage Example:**

```java
@Autowired
private ExcelDataProvider excelData;

// Use in TestNG data provider
@DataProvider
public Object[][] getUserData() {
    return excelData.getTestData(
        "data/users.xlsx",     // File path
        "valid_users",         // Sheet name
        "test_case",           // Filter column
        "TC001"                // Filter value
    );
}

@Test(dataProvider = "getUserData")
public void testWithExcelData(Map<String, String> data) {
    // Use data from Excel
    String username = data.get("username");
    String password = data.get("password");
}
```

## 4. CI/CD Support

### Docker Integration

Supports running tests in containerized environments.

**Key Features:**
- Docker Compose configuration
- Selenium Grid integration
- Remote WebDriver configuration
- Parallel test execution in containers

**Usage Example:**

```bash
# Start Selenium Grid and test containers
docker-compose up -d

# Run tests using Docker
docker-compose run test-runner
```

### GitHub Actions Workflow

Automates test execution in CI environment.

**Key Features:**
- Configurable test suites
- Environment selection
- Artifact publishing
- Test result reporting

**Usage:**
- Push to main/develop branches triggers tests
- Manual workflow dispatch with parameters
- Test reports published as artifacts

## 5. API Testing Framework

### REST Assured Integration

Provides API testing capabilities integrated with the framework.

**Key Features:**
- Fluent API for HTTP requests
- Request/response specifications
- Authentication handling
- JSON Schema validation
- Response parsing and assertions

**Usage Example:**

```java
public class ApiTest extends SpringBaseApiTest {
    
    @Test
    public void testGetEndpoint() {
        // Send GET request
        Response response = get("/users/1");
        
        // Assert response
        assertEquals(response.getStatusCode(), 200);
        assertEquals(jsonPath(response).getString("name"), "John Doe");
        
        // Validate against schema
        validateSchema(response, "schemas/user-schema.json");
    }
    
    @Test
    public void testPostEndpoint() {
        // Create payload
        UserDTO user = new UserDTO("John", "Doe", "john@example.com");
        
        // Send POST request
        Response response = post("/users", user);
        
        // Assert response
        assertEquals(response.getStatusCode(), 201);
        assertNotNull(jsonPath(response).getString("id"));
    }
}
```
