package com.ssandeep79.springseleniumdemo.demo.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

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
    
    /**
     * Creates a basic request specification
     * @return configured request specification
     */
    public RequestSpecification getBasicRequestSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON);
                
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
                .expectStatusCode(status -> status >= 200 && status < 300);
                
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
}
