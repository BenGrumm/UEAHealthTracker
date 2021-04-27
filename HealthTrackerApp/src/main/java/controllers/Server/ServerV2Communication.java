package controllers.Server;

import model.Group;
import model.User;
import org.json.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class ServerV2Communication {

    private static final String baseURL = "http://localhost:8080";

    public static void main(String[] args) {

    }

//    public static HTTPJSONResponse notifyGroupOfGoalCompletion(Group group, User user, Goal goal){
//
//    }

    public static HTTPJSONResponse emailUserAboutGroup(String email, Group group){
        return null;
    }

    public static HTTPJSONResponse addUserToGroup(String groupCode, User user){
        return null;
    }

    public static HTTPJSONResponse addNewGroup(Group group){
        return null;
    }

//    public static HTTPJSONResponse addGoalToGroup(Goal goal){
//
//    }

    public static HTTPJSONResponse sendUserToServer(User user){
        String json = formatUserToJson(user);

        try {
            HTTPJSONResponse response = sendPostRequestWithJson("/users", json);
            return response;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private static HTTPJSONResponse sendPostRequestWithJson(String relativeURLExtension, String json) throws IOException {
        URL url = new URL(baseURL + relativeURLExtension);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        addAuthToRequest(con);

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        writeStringToConnection(con, json);

        return getResponseFromCon(con);

    }

    private static void addAuthToRequest(HttpURLConnection con){
        String auth = "admin:password";
        String authHeader = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        con.setRequestProperty("Authorization", authHeader);
    }

    public static void writeStringToConnection(HttpURLConnection con, String json) throws IOException{
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
    }

    public static HTTPJSONResponse getResponseFromCon(HttpURLConnection con) throws IOException {

        int responseCode = con.getResponseCode();

        Map<String, List<String>> map = con.getHeaderFields();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;

            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            return new HTTPJSONResponse(responseCode, map, new JSONObject(response.toString()));
        }
    }

    private static final String userJsonFormat =
            "{\"email\":\"%s\",\"firstName\":\"%s\",\"surname\":\"%s\"," +
                    "\"username\":\"%s\",\"password\":\"%s\",\"weightStone\":%d," +
                    "\"weightPounds\":%d,\"height\":%f,\"gender\":\"%s\"}";
    public static String formatUserToJson(User user){
        return String.format(userJsonFormat, user.getEmail(), user.getFirstName(), user.getSurname(), user.getUsername(),
                user.getPassword(), user.getWeightStone(), user.getWeightPounds(), user.getHeight(), user.getGender().toString());
    }

}
