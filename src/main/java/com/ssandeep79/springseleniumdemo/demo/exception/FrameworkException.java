package com.ssandeep79.springseleniumdemo.demo.exception;

import org.openqa.selenium.WebElement;

/**
 * Base class for all framework-specific exceptions
 * Provides consistent exception handling across the framework
 */
public class FrameworkException extends RuntimeException {
    
    private final ExceptionType type;
    
    /**
     * Creates a new FrameworkException with specified type and message
     * @param type the type of exception
     * @param message the error message
     */
    public FrameworkException(ExceptionType type, String message) {
        super(message);
        this.type = type;
    }
    
    /**
     * Creates a new FrameworkException with specified type, message, and cause
     * @param type the type of exception
     * @param message the error message
     * @param cause the underlying cause
     */
    public FrameworkException(ExceptionType type, String message, Throwable cause) {
        super(message, cause);
        this.type = type;
    }
    
    /**
     * Gets the exception type
     * @return exception type
     */
    public ExceptionType getType() {
        return type;
    }
    
    /**
     * Enumeration of exception types for categorization
     */
    public enum ExceptionType {
        ELEMENT_NOT_FOUND,
        ELEMENT_STATE_ERROR,
        TIMEOUT,
        VALIDATION_ERROR,
        CONFIG_ERROR,
        DATA_ERROR,
        API_ERROR,
        PAGE_ERROR,
        BROWSER_ERROR,
        UNKNOWN
    }
    
    /**
     * Creates an element not found exception
     * @param locator the locator that failed to find the element
     * @return the exception
     */
    public static FrameworkException elementNotFound(String locator) {
        return new FrameworkException(
                ExceptionType.ELEMENT_NOT_FOUND,
                "Element not found with locator: " + locator
        );
    }
    
    /**
     * Creates an element state exception
     * @param element the element with the state issue
     * @param expectedState the expected state description
     * @return the exception
     */
    public static FrameworkException elementStateError(WebElement element, String expectedState) {
        return new FrameworkException(
                ExceptionType.ELEMENT_STATE_ERROR,
                "Element state error - Expected: " + expectedState
        );
    }
    
    /**
     * Creates a timeout exception
     * @param operation the operation that timed out
     * @param durationSeconds the timeout duration in seconds
     * @return the exception
     */
    public static FrameworkException timeout(String operation, int durationSeconds) {
        return new FrameworkException(
                ExceptionType.TIMEOUT,
                "Timeout after " + durationSeconds + " seconds waiting for: " + operation
        );
    }
    
    /**
     * Creates a validation exception
     * @param message the validation message
     * @return the exception
     */
    public static FrameworkException validationError(String message) {
        return new FrameworkException(
                ExceptionType.VALIDATION_ERROR,
                "Validation error: " + message
        );
    }
    
    /**
     * Creates a configuration exception
     * @param configKey the configuration key with the issue
     * @param message the error message
     * @return the exception
     */
    public static FrameworkException configError(String configKey, String message) {
        return new FrameworkException(
                ExceptionType.CONFIG_ERROR,
                "Configuration error for key '" + configKey + "': " + message
        );
    }
    
    /**
     * Creates a data error exception
     * @param message the error message
     * @return the exception
     */
    public static FrameworkException dataError(String message) {
        return new FrameworkException(
                ExceptionType.DATA_ERROR,
                "Data error: " + message
        );
    }
    
    /**
     * Creates an API error exception
     * @param endpoint the API endpoint
     * @param statusCode the HTTP status code
     * @param message the error message
     * @return the exception
     */
    public static FrameworkException apiError(String endpoint, int statusCode, String message) {
        return new FrameworkException(
                ExceptionType.API_ERROR,
                "API error for endpoint '" + endpoint + "' with status code " + statusCode + ": " + message
        );
    }
}
