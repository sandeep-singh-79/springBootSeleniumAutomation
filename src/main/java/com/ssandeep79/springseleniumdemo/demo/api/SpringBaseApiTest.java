package com.ssandeep79.springseleniumdemo.demo.api;

import com.ssandeep79.springseleniumdemo.SpringBaseTestNGTest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;

/**
 * Base class for API tests
 * Extends the main test class and provides API testing capabilities
 */
public abstract class SpringBaseApiTest extends SpringBaseTestNGTest {
    
    @Autowired
    protected ApiClientConfig apiConfig;
    
    @BeforeClass
    public void setupApiTest() {
        RestAssured.baseURI = apiConfig.getBaseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
    
    /**
     * Performs a GET request to the specified endpoint
     * @param endpoint API endpoint path
     * @return Response object
     */
    protected Response get(String endpoint) {
        return given()
                .spec(apiConfig.getBasicRequestSpec())
                .when()
                .get(endpoint)
                .then()
                .spec(apiConfig.getSuccessResponseSpec())
                .extract()
                .response();
    }
    
    /**
     * Performs a POST request with payload to the specified endpoint
     * @param endpoint API endpoint path
     * @param payload request payload object (will be serialized to JSON)
     * @return Response object
     */
    protected Response post(String endpoint, Object payload) {
        return given()
                .spec(apiConfig.getBasicRequestSpec())
                .body(payload)
                .when()
                .post(endpoint)
                .then()
                .spec(apiConfig.getSuccessResponseSpec())
                .extract()
                .response();
    }
    
    /**
     * Performs a PUT request with payload to the specified endpoint
     * @param endpoint API endpoint path
     * @param payload request payload object (will be serialized to JSON)
     * @return Response object
     */
    protected Response put(String endpoint, Object payload) {
        return given()
                .spec(apiConfig.getBasicRequestSpec())
                .body(payload)
                .when()
                .put(endpoint)
                .then()
                .spec(apiConfig.getSuccessResponseSpec())
                .extract()
                .response();
    }
    
    /**
     * Performs a DELETE request to the specified endpoint
     * @param endpoint API endpoint path
     * @return Response object
     */
    protected Response delete(String endpoint) {
        return given()
                .spec(apiConfig.getBasicRequestSpec())
                .when()
                .delete(endpoint)
                .then()
                .spec(apiConfig.getSuccessResponseSpec())
                .extract()
                .response();
    }
    
    /**
     * Extracts JsonPath from a response for easier assertions and data extraction
     * @param response the response object
     * @return JsonPath object
     */
    protected JsonPath jsonPath(Response response) {
        return response.jsonPath();
    }
    
    /**
     * Validates response against a JSON schema
     * @param response the response to validate
     * @param schemaPath path to the JSON schema file (in classpath)
     */
    protected void validateSchema(Response response, String schemaPath) {
        response.then().assertThat()
                .body(io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
    }
}
