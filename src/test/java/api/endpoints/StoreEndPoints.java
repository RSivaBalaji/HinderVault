package api.endpoints;

import static io.restassured.RestAssured.given;

import org.json.JSONObject;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class StoreEndPoints 
{
	public static Response createUser(JSONObject payload)
	{
			Response response = given()
	            .contentType(ContentType.JSON)
	            .accept(ContentType.JSON)
	            .body(String.valueOf(payload))
	        	.post(Routes.Gpost_URL);
			return response;
	}
		
	public static Response readUser(int id)
	{
	        Response response = given()
	            .accept(ContentType.JSON)
	            .contentType(ContentType.JSON)
	            .pathParam("id", id)
	        .when() 
	            .get(Routes.Gget_URL);
	        return response;
	}
	 
	 public static Response deleteUser(int id)
	 {
	        Response response = given()
	            .pathParam("id", id)
	        .when()
	            .delete(Routes.Gdelete_URL);
	        return response;
	 }
	 
	 public static Response Inventory()
	 {
	        Response response = given()
	            .accept(ContentType.JSON)
	            .contentType(ContentType.JSON)
	        .when() 
	            .get(Routes.GInventory);
	        return response;
	 }
}
