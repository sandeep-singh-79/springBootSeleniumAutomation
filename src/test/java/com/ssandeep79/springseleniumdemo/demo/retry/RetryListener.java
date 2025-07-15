package com.ssandeep79.springseleniumdemo.demo.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * TestNG listener to automatically apply RetryAnalyzer to test methods
 * This avoids having to manually add RetryAnalyzer to each test
 */
public class RetryListener implements IAnnotationTransformer {
    private static final Logger logger = LoggerFactory.getLogger(RetryListener.class);

    @Override
    public void transform(ITestAnnotation annotation, Class<?> testClass, Constructor<?> testConstructor, Method testMethod) {
        // Check if method has @RetryTest annotation
        if (testMethod != null && testMethod.isAnnotationPresent(RetryTest.class)) {
            // Apply retry analyzer only to methods with @RetryTest annotation
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
            logger.debug("Applied RetryAnalyzer to method: {}", testMethod.getName());
        }
    }
}
