package com.ssandeep79.springseleniumdemo.demo.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.ConnectionConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

/**
 * Base class for API client configuration
 * Provides common REST Assured configurations and specifications
 */
@Component
public class ApiClientConfig {

    @Value("${api.base.url:https://api.example.com}")
    private String baseUrl;
    
    @Value("${api.auth.token:}")
    private String authToken;
    
    @Value("${api.log.request:true}")
    private boolean logRequest;
    
    @Value("${api.log.response:true}")
    private boolean logResponse;
    
    @Value("${api.connection.timeout:10000}")
    private int connectionTimeout;
    
    @Value("${api.read.timeout:30000}")
    private int readTimeout;
    
    @Value("${api.retry.max:1}")
    private int maxRetry;
    
    @Value("${api.retry.delay:1000}")
    private int retryDelay;
    
    /**
     * Creates a basic request specification
     * @return configured request specification
     */
    public RequestSpecification getBasicRequestSpec() {
        // Configure timeouts
        RestAssuredConfig config = RestAssured.config()
            .connectionConfig(ConnectionConfig.connectionConfig()
                .closeIdleConnectionsAfterEachResponse())
            .httpClient(HttpClientConfig.httpClientConfig()
                .setParam("http.connection.timeout", connectionTimeout)
                .setParam("http.socket.timeout", readTimeout));
        
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(config);
                
        if (logRequest) {
            builder.log(LogDetail.ALL);
        }
        
        if (authToken != null && !authToken.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }
        
        return builder.build();
    }
    
    /**
     * Creates a request specification with custom headers
     * @param headers map of header name/value pairs
     * @return configured request specification
     */
    public RequestSpecification getRequestSpecWithHeaders(Map<String, String> headers) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .addRequestSpecification(getBasicRequestSpec());
                
        headers.forEach(builder::addHeader);
        
        return builder.build();
    }
    
    /**
     * Creates a basic success response specification (2xx status codes)
     * @return configured response specification
     */
    public ResponseSpecification getSuccessResponseSpec() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder()
                .expectStatusCode(org.hamcrest.Matchers.allOf(
                    org.hamcrest.Matchers.greaterThanOrEqualTo(200),
                    org.hamcrest.Matchers.lessThan(300)
                ));
                
        if (logResponse) {
            builder.log(LogDetail.ALL);
        }
        
        return builder.build();
    }
    
    /**
     * Creates a response specification for a specific status code
     * @param statusCode expected HTTP status code
     * @return configured response specification
     */
    public ResponseSpecification getResponseSpecForStatus(int statusCode) {
        ResponseSpecBuilder builder = new ResponseSpecBuilder()
                .expectStatusCode(statusCode);
                
        if (logResponse) {
            builder.log(LogDetail.ALL);
        }
        
        return builder.build();
    }
    
    /**
     * Gets the base API URL
     * @return base URL
     */
    public String getBaseUrl() {
        return baseUrl;
    }
    
    /**
     * Sets the base API URL
     * @param baseUrl the new base URL
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    /**
     * Gets the authentication token
     * @return auth token
     */
    public String getAuthToken() {
        return authToken;
    }
    
    /**
     * Sets the authentication token
     * @param authToken the new auth token
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    
    /**
     * Gets the connection timeout in milliseconds
     * @return connection timeout
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }
    
    /**
     * Gets the read timeout in milliseconds
     * @return read timeout
     */
    public int getReadTimeout() {
        return readTimeout;
    }
    
    /**
     * Gets the maximum number of retry attempts
     * @return maximum retry count
     */
    public int getMaxRetry() {
        return maxRetry;
    }
    
    /**
     * Gets the delay between retry attempts in milliseconds
     * @return retry delay
     */
    public int getRetryDelay() {
        return retryDelay;
    }
    
    /**
     * Creates a retry configuration object for use in tests
     * @return retry configuration
     */
    public RetryConfig getRetryConfig() {
        return new RetryConfig(maxRetry, retryDelay);
    }
    
    /**
     * Configuration class for API request retries
     */
    public static class RetryConfig {
        private final int maxRetries;
        private final int delayMs;
        
        public RetryConfig(int maxRetries, int delayMs) {
            this.maxRetries = maxRetries;
            this.delayMs = delayMs;
        }
        
        public int getMaxRetries() {
            return maxRetries;
        }
        
        public int getDelayMs() {
            return delayMs;
        }
    }
}
