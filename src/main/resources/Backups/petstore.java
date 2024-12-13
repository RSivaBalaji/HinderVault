package Backups;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class petstore 
{
    // Method to create a new user using POST
    public String createUser()
    {
        // Initialize Faker to generate random data
        Faker faker = new Faker();

        // Generate random data for the POST request
        String username = faker.name().username();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String phone = faker.phoneNumber().phoneNumber();
        int userStatus = faker.number().numberBetween(0, 2); // Random userStatus 0 or 1

        // Set the base URI for the API
        RestAssured.baseURI = "https://petstore.swagger.io/v2";

        // Create the JSON payload for the POST request
        String requestBody = "{\n" +
                "  \"id\": 0,\n" +
                "  \"username\": \"" + username + "\",\n" +
                "  \"firstName\": \"" + firstName + "\",\n" +
                "  \"lastName\": \"" + lastName + "\",\n" +
                "  \"email\": \"" + email + "\",\n" +
                "  \"password\": \"" + password + "\",\n" +
                "  \"phone\": \"" + phone + "\",\n" +
                "  \"userStatus\": " + userStatus + "\n" +
                "}";

        // Send the POST request to create a new user
        Response postResponse = RestAssured.given()
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post("/user");
        		postResponse.then().log().all();

        // Assert the POST response status code
        Assert.assertEquals(postResponse.getStatusCode(), 200, "Expected status code is 200 for POST request");

        // Return the username created
        return username;
    }

    // Method to fetch user details using GET
    public void getUserDetails(String username) 
    {
        // Set the base URI for the API
        RestAssured.baseURI = "https://petstore.swagger.io/v2";

        // Send the GET request to fetch the created user details
        Response getResponse = RestAssured.given()
                .header("accept", "application/json")
                .get("/user/" + username); // Using the created username in the GET request
        		getResponse.then().log().all();

        // Assert the GET response status code
        Assert.assertEquals(getResponse.getStatusCode(), 200, "Expected status code is 200 for GET request");
    }

    @Test
    public void testCreateAndGetUser() 
    {
        // Step 1: Create a user and get the username
        String createdUsername = createUser();

        // Step 2: Fetch user details using the username from POST request
        getUserDetails(createdUsername);
    }
}
