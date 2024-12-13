package api.testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import api.endpoints.PetEndPoints;
import api.payload.*;
import api.payload.Pet.Category;
import api.payload.Pet.Tag;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class PetTest 
{
    Faker faker;
    Pet petPayload;
    JSONObject payload;
    public Logger logger;
    public String photoUrl;

    @BeforeClass
    public void setup() throws IOException 
    {
    	// Load properties file
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        properties.load(fis);
        photoUrl = properties.getProperty("photoUrl"); 
        
    	// Initialize other variables
        faker = new Faker();
        petPayload = new Pet();

        petPayload.setId(faker.number().hashCode());
        Category category = new Category();
        category.setId(faker.number().randomDigitNotZero());
        category.setName(faker.name().firstName());
        petPayload.setCategory(category);
        petPayload.setName(faker.name().fullName());
        petPayload.setPhotoUrls(Arrays.asList(photoUrl));

        Tag tag = new Tag();
        tag.setId(faker.number().randomDigitNotZero());
        tag.setName(faker.name().lastName());
        petPayload.setTags(Arrays.asList(tag));
        
        //Dynamic status setup
        String[] validStatuses = {"available", "pending", "sold"};
        petPayload.setStatus(validStatuses[faker.number().numberBetween(0, validStatuses.length)]);

        logger = LogManager.getLogger(this.getClass());
    }

    @Test(priority = 1)
    public void testCreatePet() 
    {
        payload = new JSONObject();
        payload.put("id", petPayload.getId());
        payload.put("name", petPayload.getName());
        payload.put("category", new JSONObject()
            .put("id", petPayload.getCategory().getId())
            .put("name", petPayload.getCategory().getName()));
        payload.put("photoUrls", petPayload.getPhotoUrls());
        payload.put("tags", new JSONArray(Arrays.asList(
            new JSONObject().put("id", petPayload.getTags().get(0).getId())
                            .put("name", petPayload.getTags().get(0).getName())
        )));
        payload.put("status", petPayload.getStatus());

        logger.info("Creating the Pet");
        Response response = PetEndPoints.createPet(payload.toString());
        response.then().log().body();

        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("Pet is Created");
    }

    @Test(priority = 2, dependsOnMethods = {"testCreatePet"})
    public void testGetPetById() 
    {
        logger.info("Reading the Pet Info");
        Response getResponse = PetEndPoints.readPet(String.valueOf(petPayload.getId()));
        getResponse.then().log().body();
        Assert.assertEquals(getResponse.getStatusCode(), 200);

        getResponse.then().assertThat()
            .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/pet-schema.json")));

        logger.info("Pet Info Is Displayed\n");
    }

    @Test(priority = 3)
    public void testUpdatePet() 
    {
        logger.info("Updating the Pet Information");
        
        // Set a random name
        petPayload.setName(faker.name().firstName());

        // Define a list of valid statuses and select one randomly
        String[] validStatuses = {"available", "pending", "sold"};
        String randomStatus = validStatuses[faker.number().numberBetween(2, validStatuses.length)];
        
        // Set the random status
        petPayload.setStatus(randomStatus);

        // Update the payload with the new pet name and status
        payload.put("name", petPayload.getName());
        payload.put("status", petPayload.getStatus());

        System.out.println("Updated Pet Name: " + petPayload.getName() + ", Status: " + petPayload.getStatus());
        
        logger.info("Checking the Pet Information After Applied the Changes");
        // Call the update endpoint
        Response response = PetEndPoints.updatePet(payload.toString());
        response.then().log().body();

        Assert.assertEquals(response.getStatusCode(), 200);

        // Verify the update by reading the pet information again
        Response responseAfterUpdate = PetEndPoints.readPet(String.valueOf(petPayload.getId()));
        responseAfterUpdate.then().log().body();
        Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
        logger.info("Pet Information is Updated");
        
        logger.info("Pet Information Changes Added in the Payload");       
    }

    @Test(priority = 4)
    public void testDeletePet()
    {
        logger.info("Deleting the Pet");
        Response deleteResponse = PetEndPoints.deletePet(String.valueOf(petPayload.getId()));
        deleteResponse.then().log().all();

        Assert.assertEquals(deleteResponse.getStatusCode(), 200);
        logger.info("Pet is Deleted");

        logger.info("Verify After Delete the user, Still the Pet User Exists!");
        Response responseAfterDelete = PetEndPoints.readPet(String.valueOf(petPayload.getId()));
        responseAfterDelete.then().log().body();
        Assert.assertEquals(responseAfterDelete.getStatusCode(), 404);
        logger.info("Pet User is Deleted Completely");
    }
}