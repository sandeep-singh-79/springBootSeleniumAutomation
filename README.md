# Spring Boot Selenium Automation Framework

A powerful, flexible test automation framework built with Spring Boot, Selenium WebDriver, TestNG, and Cucumber. This framework supports both traditional TestNG tests and BDD-style tests with Cucumber.

## Key Features

- **Spring Boot Integration**: Leverages Spring's dependency injection for clean, modular test code
- **Dual Testing Approach**: Supports both BDD (Cucumber) and traditional TestNG testing
- **Page Object Pattern**: Well-structured page objects with component-based approach
- **Thread Safety**: Thread-local WebDriver management for parallel test execution
- **Cross Browser Testing**: Chrome, Firefox, and remote WebDriver support
- **Reporting**: Integrated Extent Reports and screenshot capture
- **AOP Features**: Aspect-oriented programming for cross-cutting concerns

## Technology Stack

- Java 17+
- Spring Boot 3.1.x
- Selenium WebDriver 4.23.x
- TestNG 7.10.x
- Cucumber 7.18.x
- WebDriverManager 5.9.x
- Extent Reports 5.1.x

## Prerequisites

- Java JDK 17 or higher
- Maven 3.8 or higher
- Chrome and/or Firefox browsers installed
- Git

## Quick Start

### 1. Clone the repository

```bash
git clone https://github.com/sandeep-singh-79/springBootSeleniumAutomation.git
cd springBootSeleniumAutomation
```

### 2. Build the project

```bash
mvn clean install -DskipTests
```

### 3. Run tests

Run BDD tests:

```bash
mvn test
```

Run specific TestNG suite:

```bash
mvn test -DsuiteXmlFile=testng-suite.xml
```

## Project Structure

```plaintext
src
├── main
│   ├── java
│   │   └── com/ssandeep79/springseleniumdemo
│   │       ├── demo          # Framework core components
│   │       ├── entity        # Data entities
│   │       ├── model         # Data models
│   │       ├── page          # Page objects
│   │       └── repository    # Data repositories
│   └── resources             # Application properties
├── test
│   ├── java
│   │   └── com/ssandeep79/springseleniumdemo
│   │       ├── bdd           # BDD step definitions and hooks
│   │       └── visa, flights, etc. # Test classes
│   └── resources
│       ├── features          # Cucumber feature files
│       └── data              # Test data
```

## Configuration

The framework uses Spring profiles for environment-specific configurations. Main configuration properties:

- `application.properties`: Default configuration
- `application-qa.properties`: QA environment specific settings
- `application-remote.properties`: Remote WebDriver configuration

Key configuration properties:

- `browser`: Sets the browser (chrome, firefox)
- `application.url`: Base URL for tests
- `screenshot.path`: Path for screenshots

## Detailed Documentation

- [BDD Testing Guide](docs/BDD_TESTING_GUIDE.md) - How to write BDD tests with Cucumber
- [TestNG Guide](docs/TESTNG_GUIDE.md) - How to write traditional TestNG tests
- [Common Patterns Guide](docs/COMMON_PATTERNS.md) - Best practices and common patterns
- [CI/CD Guide](docs/CICD_GUIDE.md) - Instructions for CI/CD integration

## Continuous Integration

This framework is designed to work with popular CI/CD tools. For detailed instructions on integrating with CI/CD platforms like GitHub Actions, Jenkins, and GitLab CI, see the [CI/CD Guide](docs/CICD_GUIDE.md).

## License

This project is licensed under the [Apache License 2.0](LICENSE). This is a permissive license that allows you to:

- Use the code for commercial purposes
- Modify the code
- Distribute the code
- Place warranty

The license requires:

- Including a copy of the license in any redistributed code
- Including appropriate attribution notices

For more details, see the [LICENSE](LICENSE) file in the repository.

## Version Control and Contributions

This project follows Git Flow for version control:

- `main` branch contains stable releases
- `develop` branch contains latest development changes
- Feature branches should be created from `develop`

### Contribution Guidelines

1. Fork the repository
2. Create a feature branch from `develop`
3. Make your changes
4. Run tests locally to ensure they pass
5. Submit a pull request to the `develop` branch

## Support

For questions or issues, please open an issue in the repository.
