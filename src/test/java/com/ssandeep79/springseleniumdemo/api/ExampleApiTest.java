package com.ssandeep79.springseleniumdemo.api;

import com.ssandeep79.springseleniumdemo.demo.api.SpringBaseApiTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Example API test class
 * Demonstrates how to use the API testing framework
 */
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
    }
}
