import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * An example of how to use HttpRequest class. Here I just print both responses,
 * however, if the response was a JSON string it could be converted to string instead.
 *
 * @author Luka Kralj
 * @version 22 December 2018
 */
public class Example {

    public static void main(String[] args) {
        System.out.println("Example without parameters: \n");
        try {
            String response = HttpRequest.getResponse("GET", "https://www.google.com/", "");
            System.out.println(response);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\n===========================================\n");

        System.out.println("Example with parameters: \n");

        Map<String, String> params = new HashMap<>();
        params.put("search_query", "pink+floyd");
        // Constructs : https://www.youtube.com/results?search_query=pink+floyd
        try {
            String response = HttpRequest.getResponse("GET", "https://www.youtube.com/results", HttpRequest.createParameters(params));
            System.out.println(response);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
