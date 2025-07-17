package com.ssandeep79.springseleniumdemo;

import static io.restassured.RestAssured.given;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.testng.annotations.BeforeClass;

import com.ssandeep79.springseleniumdemo.demo.api.ApiClientConfig;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/**
 * Base class for API tests
 * Extends the main test class and provides API testing capabilities
 */
public abstract class SpringBaseApiTest extends SpringBaseTestNGTest {

    @Autowired
    protected ApiClientConfig apiConfig;
    
    @Autowired
    protected Environment environment;

    @BeforeClass
    public void setupApiTest () {
        RestAssured.baseURI = apiConfig.getBaseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        
        // Log API configuration for the current environment
        System.out.println("API Configuration: ");
        System.out.println("  Base URL: " + apiConfig.getBaseUrl());
        System.out.println("  Connection Timeout: " + apiConfig.getConnectionTimeout() + "ms");
        System.out.println("  Read Timeout: " + apiConfig.getReadTimeout() + "ms");
        System.out.println("  Max Retries: " + apiConfig.getMaxRetry());
        System.out.println("  Retry Delay: " + apiConfig.getRetryDelay() + "ms");
    }

    /**
     * Performs a GET request to the specified endpoint
     *
     * @param endpoint API endpoint path
     * @return Response object
     */
    protected Response get(String endpoint) {
        return executeWithRetry(() -> 
            given()
                .spec(apiConfig.getBasicRequestSpec())
                .when()
                .get(endpoint)
                .then()
                .spec(apiConfig.getSuccessResponseSpec())
                .extract()
                .response()
        );
    }

    /**
     * Interface for API operations that can be retried
     */
    @FunctionalInterface
    protected interface ApiOperation<T> {
        T execute() throws Exception;
    }

    /**
     * Executes an API operation with retry logic based on configuration
     * 
     * @param operation The API operation to execute
     * @return The result of the operation
     */
    protected <T> T executeWithRetry(ApiOperation<T> operation) {
        int retries = apiConfig.getMaxRetry();
        int delay = apiConfig.getRetryDelay();
        Exception lastException = null;
        
        for (int attempt = 0; attempt <= retries; attempt++) {
            try {
                return operation.execute();
            } catch (Exception e) {
                lastException = e;
                System.out.println("API request failed (attempt " + (attempt + 1) + 
                    " of " + (retries + 1) + "): " + e.getMessage());
                
                if (attempt < retries) {
                    try {
                        System.out.println("Retrying in " + delay + "ms...");
                        TimeUnit.MILLISECONDS.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Retry interrupted", ie);
                    }
                }
            }
        }
        
        // If we got here, all retries failed
        throw new RuntimeException("API request failed after " + (retries + 1) + 
            " attempts", lastException);
    }

    /**
     * Performs a POST request with payload to the specified endpoint
     *
     * @param endpoint API endpoint path
     * @param payload  request payload object (will be serialized to JSON)
     * @return Response object
     */
    protected Response post(String endpoint, Object payload) {
        return executeWithRetry(() ->
            given()
                .spec(apiConfig.getBasicRequestSpec())
                .body(payload)
                .when()
                .post(endpoint)
                .then()
                .spec(apiConfig.getSuccessResponseSpec())
                .extract()
                .response()
        );
    }

    /**
     * Performs a PUT request with payload to the specified endpoint
     *
     * @param endpoint API endpoint path
     * @param payload  request payload object (will be serialized to JSON)
     * @return Response object
     */
    protected Response put(String endpoint, Object payload) {
        return executeWithRetry(() ->
            given()
                .spec(apiConfig.getBasicRequestSpec())
                .body(payload)
                .when()
                .put(endpoint)
                .then()
                .spec(apiConfig.getSuccessResponseSpec())
                .extract()
                .response()
        );
    }

    /**
     * Performs a DELETE request to the specified endpoint
     *
     * @param endpoint API endpoint path
     * @return Response object
     */
    protected Response delete(String endpoint) {
        return executeWithRetry(() ->
            given()
                .spec(apiConfig.getBasicRequestSpec())
                .when()
                .delete(endpoint)
                .then()
                .spec(apiConfig.getSuccessResponseSpec())
                .extract()
                .response()
        );
    }

    /**
     * Extracts JsonPath from a response for easier assertions and data extraction
     *
     * @param response the response object
     * @return JsonPath object
     */
    protected JsonPath jsonPath (Response response) {
        return response.jsonPath();
    }

    /**
     * Validates response against a JSON schema
     *
     * @param response   the response to validate
     * @param schemaPath path to the JSON schema file (in classpath)
     */
    protected void validateSchema (Response response, String schemaPath) {
        response.then().assertThat()
            .body(io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
    }
}
