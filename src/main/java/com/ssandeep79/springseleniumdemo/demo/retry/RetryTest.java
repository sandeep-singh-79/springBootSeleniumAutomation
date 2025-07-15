package com.ssandeep79.springseleniumdemo.demo.retry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark test methods for retry with custom configuration
 * Use this on test methods that should be retried on failure
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RetryTest {
    /**
     * Maximum number of retry attempts
     * @return max retry count
     */
    int maxRetryCount() default 2;
    
    /**
     * Delay between retry attempts in milliseconds
     * @return delay in milliseconds
     */
    long delayBetweenRetriesMs() default 1000;
    
    /**
     * Optional description of why this test requires retry
     * @return description
     */
    String description() default "";
    
    /**
     * Whether to retry only for specific exception types
     * @return array of exception classes that should trigger retry
     */
    Class<? extends Throwable>[] retryFor() default {};
}
