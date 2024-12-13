package api.testcases;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import api.utilities.DataProviders;
import io.restassured.response.Response;

public class DataDrivenTests 
{
	User userPayload;
	@Test(priority = 1, dataProvider = "Data", dataProviderClass = DataProviders.class)
	public void testPostUser(String UserID, String UserName, String fname, String lname, String useremail, String pwd, String Phone)
	{
		userPayload = new User();
		
		userPayload.setId(Integer.parseInt(UserID));
		userPayload.setUserName(UserName);
		userPayload.setFirstName(fname);
		userPayload.setLastName(lname);
		userPayload.setEmail(useremail);
		userPayload.setPassword(pwd);
		userPayload.setPhone(Phone);
		
		//Create dynamic JSON payload using Faker
        JSONObject payload = new JSONObject();
        payload.put("id", userPayload.getId());
        payload.put("username", userPayload.getUserName());
        payload.put("firstName", userPayload.getFirstName());
        payload.put("lastName", userPayload.getLastName());
        payload.put("email", userPayload.getEmail());
        payload.put("password", userPayload.getPassword());
        payload.put("phone", userPayload.getPhone());

        // Create user using the dynamic payload
        Response response = UserEndPoints.createUser(payload.toString());
        //response.then().log().body();

        Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test(priority = 2, dataProvider = "UserNames", dataProviderClass = DataProviders.class)
	public void testGetUserByName(String userName)
	{
		Response getResponse = UserEndPoints.readUser(userName);
        getResponse.then().log().body();
        Assert.assertEquals(getResponse.getStatusCode(), 200);
        //System.out.println("Generated Username: " + userName);
	}    
	
	@Test(priority = 3, dataProvider = "Data", dataProviderClass = DataProviders.class)
	public void testUpdateUser(String UserID, String UserName, String fname, String lname, String useremail, String pwd, String Phone)
	{
	    userPayload = new User();

	    userPayload.setId(Integer.parseInt(UserID));
	    userPayload.setUserName(UserName);

	    // Generate dynamic values for firstName, lastName, and email using Faker
	    Faker faker = new Faker();
	    String dynamicFirstName = faker.name().firstName();  // Random first name
	    String dynamicLastName = faker.name().lastName();   // Random last name
	    String dynamicEmail = faker.internet().emailAddress(); // Random email

	    // Update the user with the dynamic values
	    userPayload.setFirstName(dynamicFirstName);
	    userPayload.setLastName(dynamicLastName);
	    userPayload.setEmail(dynamicEmail);

	    // Create dynamic JSON payload for the update
	    JSONObject payload = new JSONObject();
	    payload.put("id", userPayload.getId());
	    payload.put("username", userPayload.getUserName());
	    payload.put("firstName", userPayload.getFirstName());
	    payload.put("lastName", userPayload.getLastName());
	    payload.put("email", userPayload.getEmail());
	    payload.put("password", userPayload.getPassword());
	    payload.put("phone", userPayload.getPhone());

	    // Update user using the dynamic payload
	    Response updateResponse = UserEndPoints.updateUser(UserName, payload.toString()); // Assuming updateUser method is defined in UserEndPoints

	    // Verify that the update was successful
	    Assert.assertEquals(updateResponse.getStatusCode(), 200);

	    // Fetch the user data right after the update
	    Response getResponse = UserEndPoints.readUser(UserName);

	    // Print the GET response to the console
	    System.out.println("GET Response After Update: ");
	    getResponse.then().log().body(); // This will print the response body to the console
	}

	
	@Test(priority = 4, dataProvider = "UserNames", dataProviderClass = DataProviders.class)
	public void testDeleteUserByName(String userName)
	{
		Response getdeleteUser = UserEndPoints.deleteUser(userName);
		Assert.assertEquals(getdeleteUser.getStatusCode(), 200);
	} 
}
