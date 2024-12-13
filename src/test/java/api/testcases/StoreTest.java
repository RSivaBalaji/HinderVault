package api.testcases;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import api.endpoints.StoreEndPoints;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreTest 
{
    private Faker faker;
    private Random random;
    private Response inventory;
    private JSONObject payload;
    Logger logger = LogManager.getLogger(StoreTest.class);

    @BeforeClass
    public void setup()
    {
        faker = new Faker();
        random = new Random();
        logger.info("Test setup completed.");
    }

    @Test(priority = 1)
    public void testPostUser()
    {
        logger.info("Fetching inventory before creating an order.");
        // Fetch inventory before creating an order
        inventory = StoreEndPoints.Inventory();
        inventory.then().log().body();

        // Create payload dynamically using Faker
        logger.info("Creating payload dynamically using Faker.");
        payload = new JSONObject();
        payload.put("id", faker.idNumber().hashCode());
        payload.put("petId", faker.number().randomDigitNotZero());
        payload.put("quantity", faker.number().randomDigitNotZero());
        payload.put("shipDate", faker.date().future(10, TimeUnit.DAYS).toInstant().toString());

        // Dynamically set the "status" field 
        String[] statuses = {"placed", "shipped", "delivered"};
        String status = statuses[new Random().nextInt(statuses.length)];
        payload.put("status", status);
        logger.info("Randomly selected status: " + status);

        // Dynamically set the "complete" field
        boolean complete = new Random().nextBoolean();
        payload.put("complete", complete);
        logger.info("Randomly selected complete status: " + complete);

        // Send the POST request
        logger.info("Sending POST request to create the order.");
        Response createUser = StoreEndPoints.createUser(payload);
        createUser.then().log().all();
        
        Assert.assertEquals(createUser.getStatusCode(), 200);
        logger.info("Order created successfully.");

        // Fetch inventory after creating the order
        logger.info("Fetching inventory after creating the order.");
        inventory.then().log().body();
    }

    @Test(priority = 2, dependsOnMethods = {"testPostUser"})
    public void testGetUserbyName() throws InterruptedException 
    {
        // Use the ID that was generated during the POST request
        int generatedId = payload.getInt("id");
        logger.info("Generated ID for GET request: " + generatedId);
        
        // Send the GET request to fetch the user by ID
        logger.info("Sending GET request to fetch the user by ID.");
        Response getResponse = StoreEndPoints.readUser(generatedId);
        getResponse.then().log().all();
        
        Assert.assertEquals(getResponse.getStatusCode(), 200);
        logger.info("User fetched successfully.");
        
        getResponse.then().assertThat()
        .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/order-schema.json")));
        
        logger.info("Response validated against schema.");
    }

    @Test(priority = 3)
    public void testDeleteUserbyName()
    {
        // Use the ID that was generated during the POST request
        int generatedId = payload.getInt("id");
        logger.info("Generated ID for DELETE request: " + generatedId);
        
        // Send the DELETE request
        logger.info("Sending DELETE request to delete the user.");
        Response deleteresponse = StoreEndPoints.deleteUser(generatedId);
        deleteresponse.then().log().all();
        
        Assert.assertEquals(deleteresponse.getStatusCode(), 200);
        logger.info("User deleted successfully.");

        // Check after deleting the order
        logger.info("Sending GET request to check user after deletion.");
        Response getResponse = StoreEndPoints.readUser(generatedId);
        getResponse.then().log().all();
        Assert.assertEquals(getResponse.getStatusCode(), 404);
        logger.info("User not found after deletion.");

        // Check the inventory after deleting the order
        logger.info("Fetching inventory after deleting the order.");
        inventory.then().log().body();
    }
}
