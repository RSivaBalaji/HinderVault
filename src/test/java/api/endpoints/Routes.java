package api.endpoints;

public class Routes 
{
    public static String base_URL = "https://petstore.swagger.io/v2/";

    // User Module
    public static String post_URL = base_URL + "user";
    public static String get_URL = base_URL + "user/{username}";
    public static String update_URL = base_URL + "user/{username}";
    public static String delete_URL = base_URL + "user/{username}";
    
    // Store Module
    // Here you will create the Store Module URL's
    public static String Gpost_URL = base_URL + "store/order";
    public static String Gget_URL = base_URL + "store/order/{id}";
    public static String Gdelete_URL = base_URL + "store/order/{id}";
    public static String GInventory = base_URL + "store/inventory";
    
    // Pet Module
    // Here you will create the Pet Module URL's 
    public static String Spost_URL = base_URL + "pet";
    public static String Sget_URL = base_URL + "pet/{id}";
    public static String Sput_URL = base_URL + "pet";
	public static String Sdelete_URL = base_URL + "pet/{id}"; 
}

