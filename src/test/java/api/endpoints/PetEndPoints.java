package api.endpoints;

import static io.restassured.RestAssured.given;

import java.util.ResourceBundle;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PetEndPoints 
{	
	//Method created for getting URL's from properties file
	/*static ResourceBundle getURL() 
	{
	    ResourceBundle routes = ResourceBundle.getBundle("routes"); 
	    return routes;
	}*/
	
    public static Response createPet(String payload) 
    {
    	//String postURL = getURL().getString("postURL");
        
    	Response response = given()
            .header("Accept", "*/*")
            .header("accept", "application/json")
            .header("Content-Type", "application/json")
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(payload)
            .post(Routes.Spost_URL);
        return response;
    }

    public static Response readPet(String id) 
    {
        Response response = given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .pathParam("id", id)
        .when()
            .get(Routes.Sget_URL);
        return response;
    }

    public static Response updatePet(String payload) 
    {
        Response response = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(payload)
        .when()
            .put(Routes.Sput_URL);
        return response;
    }

    public static Response deletePet(String id) 
    {
        Response response = given()
            .pathParam("id", id)
        .when()
            .delete(Routes.Sdelete_URL);
        return response;
    }
}
