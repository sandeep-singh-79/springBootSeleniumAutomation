package com.ssandeep79.springseleniumdemo.api;

import io.restassured.response.Response;
import org.springframework.test.context.ActiveProfiles;
import org.testng.annotations.Test;

import com.ssandeep79.springseleniumdemo.SpringBaseApiTest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Example API test class
 * Demonstrates how to use the API testing framework
 */
@ActiveProfiles("qa") // Use QA environment properties
public class ExampleApiTest extends SpringBaseApiTest {
    
    /**
     * Example of a simple GET request test
     * Using a public API for demonstration
     */
    @Test
    public void testGetUsers() {
        // Override base URL for this test to use a public API
        apiConfig.setBaseUrl("https://reqres.in/api");
        
        // Send GET request to /users endpoint
        Response response = get("/users?page=1");
        
        // Assert response details
        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.getTime() < 5000); // Response time less than 5 seconds
        
        // Extract and verify response data
        int page = jsonPath(response).getInt("page");
        int perPage = jsonPath(response).getInt("per_page");
        int total = jsonPath(response).getInt("total");
        
        assertEquals(page, 1);
        assertTrue(perPage > 0);
        assertTrue(total > 0);
        
        // Verify users array exists and has items
        assertTrue(jsonPath(response).getList("data").size() > 0);
    }
    
    /**
     * Example of a POST request test
     */
    @Test
    public void testCreateUser() {
        // Override base URL for this test
        apiConfig.setBaseUrl("https://reqres.in/api");
        
        // Create user payload
        String name = "John Doe";
        String job = "QA Engineer";
        
        String payload = "{\"name\": \"" + name + "\", \"job\": \"" + job + "\"}";
        
        // Send POST request with payload
        Response response = post("/users", payload);
        
        // Assert response details
        assertEquals(response.getStatusCode(), 201);
        
        // Verify created user data
        assertEquals(jsonPath(response).getString("name"), name);
        assertEquals(jsonPath(response).getString("job"), job);
        assertTrue(jsonPath(response).getString("id") != null);
        assertTrue(jsonPath(response).getString("createdAt") != null);
        
        // Log the current environment configuration
        System.out.println("Current Active Profiles: " + String.join(", ", environment.getActiveProfiles()));
        System.out.println("Using retry configuration: " + apiConfig.getMaxRetry() + " retries with " + 
                          apiConfig.getRetryDelay() + "ms delay");
    }
    
    /**
     * Example of using environment-specific configuration
     */
    @Test
    public void testEnvironmentSpecificConfig() {
        System.out.println("Current environment: " + String.join(", ", environment.getActiveProfiles()));
        System.out.println("API Base URL: " + apiConfig.getBaseUrl());
        System.out.println("Connection Timeout: " + apiConfig.getConnectionTimeout());
        System.out.println("Read Timeout: " + apiConfig.getReadTimeout());
        
        // This will use the configured base URL from application-qa.properties
        Response response = get("/users?page=1");
        assertEquals(response.getStatusCode(), 200);
    }
}
