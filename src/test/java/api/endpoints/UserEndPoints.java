package api.endpoints;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class UserEndPoints 
{
    public static Response createUser(String payload)  
    {
        Response response = given()							
        	.header("Accept", "*/*")
        	.header("accept", "application/json")
            .header("Content-Type", "application/json")
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(payload)  // Pass the String payload
           // .log().all()
            .post(Routes.post_URL);
        return response;
    }
    
    public static Response readUser(String userName)
    {
        Response response = given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .pathParam("username", userName)
        .when() 
        	//.log().all()
            .get(Routes.get_URL);
        return response;
    }
    
    public static Response updateUser(String userName, String payload)
    {
    	//System.out.println("Data Check: "+String.valueOf(payload));
        Response response = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .pathParam("username", userName)
            .body(payload)
        .when()
            .put(Routes.update_URL);
        return response;
    }
        
    public static Response deleteUser(String userName)
    {
        Response response = given()
            .pathParam("username", userName)
        .when()
            .delete(Routes.delete_URL);
        return response;
    }
}
