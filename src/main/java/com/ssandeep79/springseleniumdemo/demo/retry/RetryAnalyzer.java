package com.ssandeep79.springseleniumdemo.demo.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * TestNG retry analyzer with flexible retry capabilities
 * Allows for automatic retrying of failed tests with configurable limits
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(RetryAnalyzer.class);
    
    private int counter = 0;
    private int maxRetries = 2; // Default value
    private long retryDelayMs = 1000; // Default delay between retries in milliseconds
    
    @Override
    public boolean retry(ITestResult result) {
        // Check if the test has a custom retry annotation
        RetryTest annotation = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(RetryTest.class);
        
        if (annotation != null) {
            maxRetries = annotation.maxRetryCount();
            retryDelayMs = annotation.delayBetweenRetriesMs();
        }
        
        boolean shouldRetry = !result.isSuccess() && counter < maxRetries;
        
        if (shouldRetry) {
            counter++;
            logger.info("Retrying test: {} - Retry #{} of {}", result.getName(), counter, maxRetries);
            
            // Add failure info to test context for reporting
            result.setAttribute("RETRY_ATTEMPT", counter);
            
            // Sleep between retries if specified
            if (retryDelayMs > 0) {
                try {
                    Thread.sleep(retryDelayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.warn("Thread interrupted during retry delay", e);
                }
            }
            
            return true;
        }
        
        // Reset counter for next test method
        counter = 0;
        return false;
    }
    
    /**
     * Gets the current retry count
     * @return current retry counter
     */
    public int getCounter() {
        return counter;
    }
    
    /**
     * Gets the maximum retry count
     * @return max retry count
     */
    public int getMaxRetryCount() {
        return maxRetries;
    }
    
    /**
     * Gets the retry delay in milliseconds
     * @return retry delay in ms
     */
    public long getRetryDelayMs() {
        return retryDelayMs;
    }
}
