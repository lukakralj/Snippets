import java.io.IOException;
import java.util.Map;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class provides two useful method for creating and executing POST and
 * GET requests. The response returned is of String type but it can be
 * converted later, say to JSON object.
 *
 * @author Luka Kralj
 * @version 22 December 2018
 */
public class HttpRequest {

    /**
     * Convenient function for creating parameters string from a map. Parameters
     * are formed in this way: key=value?key=value?...
     *
     * @param postArguments Map to form requests parameters from.
     * @return String of all paramters concatenated together.
     */
    public static String createParameters(Map<String, String> postArguments) {
        StringBuilder builder = new StringBuilder();
        for (String key : postArguments.keySet()) {
            builder.append(key);
            builder.append("=");
            builder.append(postArguments.get(key));
            builder.append("&");
        }
        return builder.substring(0, builder.length() - 1); // remove final &
    }

    /**
     * This method executes the request and returns whatever the server returns.
     *
     * @param method Request method: GET or POST.
     * @param url Base URL of the request.
     * @param paramters Valid string that includes the request paramters (I suggest
     *                  using createParameters() method for this).
     * @return Response from the server in the string format (can be converted later
     *         to other more convenient formats).
     */
    public static String getResponse(String method, String url, String parameters) throws IOException {
        if (method.equals("GET") && parameters.length() != 0) {
            url = url + "?" + parameters;
        }
        URL u = new URL(url);

        HttpURLConnection con = (HttpURLConnection) u.openConnection();
        con.setRequestMethod(method);

        if (method.equals("POST")) {
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(parameters);
            out.flush();
            out.close();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        con.disconnect();
        return content.toString();
    }
}
