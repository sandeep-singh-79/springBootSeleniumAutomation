# CI/CD Integration Guide

This guide provides instructions for integrating the Spring Boot Selenium automation framework with various CI/CD platforms. For general framework information, refer to the [README.md](../README.md), and for information about common patterns used in the framework, see the [Common Patterns Guide](COMMON_PATTERNS.md).

## Table of Contents

- [Overview](#overview)
- [GitHub Actions](#github-actions)
- [Jenkins](#jenkins)
- [GitLab CI](#gitlab-ci)
- [Configuration Best Practices](#configuration-best-practices)
- [Reporting in CI/CD](#reporting-in-cicd)

## Overview

Continuous Integration and Continuous Deployment (CI/CD) are essential practices for maintaining software quality. This framework is designed to integrate seamlessly with popular CI/CD tools to automate testing and deployment processes.

Key CI/CD integration features:

- **Test Automation**: Run tests automatically on code changes
- **Multi-Browser Testing**: Configure CI to test across different browsers
- **Parallel Test Execution**: Speed up test suites in CI environments
- **Artifact Generation**: Generate and store test reports
- **Environment Configuration**: Run tests against different environments

## GitHub Actions

GitHub Actions is a CI/CD service integrated directly with GitHub repositories.

### GitHub Actions Basic Setup

Create a workflow file at `.github/workflows/test.yml`:

```yaml
name: Test Automation

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'adopt'
      - name: Build with Maven
        run: mvn -B clean install
      - name: Run Tests
        run: mvn test -DsuiteXmlFile=testng-suite.xml
      - name: Upload Test Results
        if: always()
        uses: actions/upload-artifact@v3
        with:
          name: test-reports
          path: target/surefire-reports/
```

### GitHub Actions Advanced Setup

For more complex scenarios like matrix testing with multiple browsers:

```yaml
jobs:
  test:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        browser: [chrome, firefox]
    steps:
      # ... checkout and setup steps ...
      - name: Run Tests
        run: mvn test -DsuiteXmlFile=testng-suite.xml -Dbrowser=${{ matrix.browser }}
```

## Jenkins

Jenkins is a self-hosted automation server that can be used for building, testing, and deploying code.

### Jenkins Basic Setup

Create a `Jenkinsfile` in the root of your project:

```groovy
pipeline {
    agent any
    
    tools {
        maven 'Maven 3.8'
        jdk 'JDK 17'
    }
    
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test -DsuiteXmlFile=testng-suite.xml'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
    }
}
```

### Jenkins Advanced Configuration

For parallel test execution and multiple environments:

```groovy
pipeline {
    agent any
    
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['qa', 'stg'], description: 'Test environment')
    }
    
    stages {
        // ... build stage ...
        stage('Test') {
            parallel {
                stage('Chrome Tests') {
                    steps {
                        sh 'mvn test -DsuiteXmlFile=testng-suite.xml -Dbrowser=chrome -Dspring.profiles.active=${params.ENVIRONMENT}'
                    }
                }
                stage('Firefox Tests') {
                    steps {
                        sh 'mvn test -DsuiteXmlFile=testng-suite.xml -Dbrowser=firefox -Dspring.profiles.active=${params.ENVIRONMENT}'
                    }
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/surefire-reports',
                        reportName: 'Test Report',
                        reportTitles: 'Test Report'
                    ])
                }
            }
        }
    }
}
```

## GitLab CI

GitLab CI/CD is a part of GitLab that can build, test, and deploy your code with each commit.

### GitLab CI Basic Setup

Create a `.gitlab-ci.yml` file:

```yaml
image: maven:3.8-openjdk-17

stages:
  - build
  - test

variables:
  MAVEN_OPTS: "-Dmaven.repo.local=.m2/repository"

cache:
  paths:
    - .m2/repository/

build:
  stage: build
  script:
    - mvn clean install -DskipTests

test:
  stage: test
  script:
    - mvn test -DsuiteXmlFile=testng-suite.xml
  artifacts:
    paths:
      - target/surefire-reports/
```

### GitLab CI Advanced Configuration

For environment-specific testing:

```yaml
.test_template: &test_template
  stage: test
  artifacts:
    paths:
      - target/surefire-reports/
    reports:
      junit: target/surefire-reports/TEST-*.xml

test:qa:
  <<: *test_template
  script:
    - mvn test -DsuiteXmlFile=testng-suite.xml -Dspring.profiles.active=qa

test:stg:
  <<: *test_template
  script:
    - mvn test -DsuiteXmlFile=testng-suite.xml -Dspring.profiles.active=stg
```

## Configuration Best Practices

1. **Environment Variables**: Use environment variables for sensitive information

   ```yaml
   # GitHub Actions example
   env:
     DB_USER: ${{ secrets.DB_USER }}
     DB_PASSWORD: ${{ secrets.DB_PASSWORD }}
   ```

2. **Caching Dependencies**: Cache Maven dependencies to speed up builds

   ```yaml
   # GitHub Actions example
   - name: Cache Maven packages
     uses: actions/cache@v3
     with:
       path: ~/.m2
       key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
   ```

3. **Scheduled Runs**: Schedule regular test runs to detect environmental issues

   ```yaml
   # GitHub Actions example
   on:
     schedule:
       - cron: '0 0 * * *'  # Run daily at midnight
   ```

4. **Test Splitting**: For large test suites, split tests across multiple runners

   ```yaml
   # GitLab CI example
   test:split:
     parallel: 3
     script:
       - mvn test -DsuiteXmlFile=testng-suite.xml -Dgroups=${CI_NODE_INDEX}
   ```

## Reporting in CI/CD

This framework generates various reports that can be integrated with CI/CD platforms:

1. **TestNG Reports**: Available in `target/surefire-reports/`
2. **Extent Reports**: HTML reports with screenshots
3. **Cucumber HTML Reports**: For BDD test execution

### Publishing Reports

Most CI/CD platforms allow publishing HTML reports:

**Jenkins Example**:

```groovy
post {
    always {
        publishHTML([
            allowMissing: false,
            alwaysLinkToLastBuild: true,
            keepAll: true,
            reportDir: 'target/cucumber-html-reports',
            reportName: 'Cucumber Report',
            reportTitles: 'Cucumber Test Results'
        ])
    }
}
```

**GitHub Actions Example** (requires setup of GitHub Pages or a third-party action):

```yaml
- name: Publish Test Report
  if: always()
  uses: actions/upload-artifact@v3
  with:
    name: cucumber-reports
    path: target/cucumber-html-reports
```

By following this guide, you can seamlessly integrate this test automation framework into your CI/CD pipeline and ensure consistent, reliable test execution with every code change.
