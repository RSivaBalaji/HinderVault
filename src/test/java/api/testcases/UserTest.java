package api.testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class UserTest 
{
    Faker faker;
    User userPayload;
    JSONObject payload;
    public Logger logger;	//For Logs

    @BeforeClass
    public void setup()
    {
        faker = new Faker();
        userPayload = new User();

        userPayload.setId(faker.idNumber().hashCode());
        userPayload.setUserName(faker.name().username().replaceAll("[^a-zA-Z0-9]", ""));
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        userPayload.setEmail(faker.internet().safeEmailAddress());
        userPayload.setPassword(faker.internet().password(5, 10));
        userPayload.setPhone(faker.phoneNumber().cellPhone());
        userPayload.setUserStatus(faker.number().randomDigitNotZero());
        
        //Initiated the Logs here:
        logger = LogManager.getLogger(this.getClass());
    }
    
    @Test(priority = 1)
    public void testPostUser()
    {   
        //Create dynamic JSON payload using Faker
        payload = new JSONObject();
        payload.put("id", userPayload.getId());
        payload.put("username", userPayload.getUserName());
        payload.put("firstName", userPayload.getFirstName());
        payload.put("lastName", userPayload.getLastName());
        payload.put("email", userPayload.getEmail());
        payload.put("password", userPayload.getPassword());
        payload.put("phone", userPayload.getPhone());
        payload.put("userStatus", userPayload.getUserStatus());

        //Create user using the dynamic payload
    	logger.info("Creating the User");
        Response response = UserEndPoints.createUser(payload.toString());
        response.then().log().body();

        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("User is Created");
    }
    
    @Test(priority = 2, dependsOnMethods = {"testPostUser"})
    public void testGetUserbyName() throws InterruptedException 
    {
    	logger.info("Reading the User Info");
        
    	Response getResponse = UserEndPoints.readUser(this.userPayload.getUserName());
        getResponse.then().log().body();
        
        Assert.assertEquals(getResponse.getStatusCode(), 200);
        
        getResponse.then().assertThat()
        .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/user-schema.json")));
       
        logger.info("Generated Username: " + userPayload.getUserName());
        
        logger.info("User Info Is Displayed");
    }   
    
    @Test(priority = 3)
    public void testUpdateUserbyName()
    {
        //Update data using Faker for firstName, lastName, email
    	logger.info("Updating the User Information");
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        userPayload.setEmail(faker.internet().safeEmailAddress());        
       
        payload.put("firstName", userPayload.getFirstName());
        payload.put("lastName", userPayload.getLastName());
        payload.put("email", userPayload.getEmail());        
        
        //Log the updated user details before sending the request
        logger.info("Updated User: " + userPayload.getFirstName() + " " + userPayload.getLastName() + " " + userPayload.getEmail());

        //Send the update request        
        Response response = UserEndPoints.updateUser(this.userPayload.getUserName(), payload.toString());
        response.then().log().body();
        
        Assert.assertEquals(response.getStatusCode(), 200);

        // Checking Data after Update
        Response responseAfterUpdate = UserEndPoints.readUser(this.userPayload.getUserName());
        responseAfterUpdate.then().log().body();
        Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
        logger.info("User Information is Updated");
    }
    
    @Test(priority = 4)
    public void testDeleteUserbyName()
    {
        logger.info("Deleting the User");
        Response deleteresponse = UserEndPoints.deleteUser(this.userPayload.getUserName());
        deleteresponse.then().log().all();

        Assert.assertEquals(deleteresponse.getStatusCode(), 200);
        logger.info("User is Deleted");
        
        //Checking the user after Delete
        Response responseAfterUpdate = UserEndPoints.readUser(this.userPayload.getUserName());
        responseAfterUpdate.then().log().body();
        Assert.assertEquals(responseAfterUpdate.getStatusCode(), 404);        
    }        
}
