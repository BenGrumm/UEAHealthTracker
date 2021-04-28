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

    public static void main(String[] args) throws IOException{
        System.out.println("Response = " + sendDeleteRequest("/users/ben.grummitt@gmail.com/groups/0").getResponseCode());
        System.out.println("Response = " + joinUserGroup("ben.grummitt@gmail.com", 0).getResponseCode());
        System.out.println("Response = " + joinUserGroup("ben.grummitt@gmail.com", 0).getResponseCode());
    }

    public static HTTPJSONResponse getUsers(){
        try {
            return sendGetRequestForJsonResponse("/users");
        }catch (IOException e){
            return null;
        }
    }

    public static HTTPJSONResponse getGroups(){
        try {
            return sendGetRequestForJsonResponse("/groups");
        }catch (IOException e){
            return null;
        }
    }

    public static HTTPJSONResponse getGoals(){
        try {
            return sendGetRequestForJsonResponse("/goals");
        }catch (IOException e){
            return null;
        }
    }

    public static HTTPJSONResponse sendGroupToServer(Group group){
        String json = formatGroupToJson(group);

        try{
            return sendPostRequestWithJson("/groups", json);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static HTTPJSONResponse sendUserToServer(User user){
        String json = formatUserToJson(user);

        try {
            return sendPostRequestWithJson("/users", json);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static HTTPResponse joinUserGroup(String userEmail, int groupId) throws IOException{
        String urlRelative = "/users/" + userEmail + "/groups";
        String urlToJoin = baseURL + "/groups/" + groupId;

        return sendPutRequestWithURI(urlRelative, urlToJoin);
    }

    private static HTTPJSONResponse sendGetRequestForJsonResponse(String relativeURLExtension) throws IOException{
        URL url = new URL(baseURL + relativeURLExtension);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        addAuthToRequest(con);

        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        return getResponseFromCon(con);
    }

    public static HTTPJSONResponse sendPostRequestWithJson(String relativeURLExtension, String json) throws IOException {
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

    public static HTTPResponse sendPutRequestWithURI(String relativeURLExtension, String uri) throws IOException{
        URL url = new URL(baseURL + relativeURLExtension);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        addAuthToRequest(con);

        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "text/uri-list; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        writeStringToConnection(con, uri);

        return new HTTPResponse(con.getResponseCode(), con.getHeaderFields());
    }

    public static HTTPResponse sendDeleteRequest(String relativeURLExtension) throws IOException{
        URL url = new URL(baseURL + relativeURLExtension);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Accept", "*/*");

        addAuthToRequest(con);

        con.setRequestMethod("DELETE");

        return new HTTPResponse(con.getResponseCode(), con.getHeaderFields());
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
            "{\"email\":\"%s\"," +
                    "\"firstName\":\"%s\"," +
                    "\"surname\":\"%s\"," +
                    "\"username\":\"%s\"," +
                    "\"password\":\"%s\"," +
                    "\"weightStone\":%d," +
                    "\"weightPounds\":%d," +
                    "\"idealWeightStone\":%d," +
                    "\"idealWeightPounds\":%d," +
                    "\"height\":%f," +
                    "\"BMI\":%f," +
                    "\"gender\":\"%s\"}";

    public static String formatUserToJson(User user){
        return String.format(userJsonFormat, user.getEmail(),
                user.getFirstName(), user.getSurname(), user.getUsername(),
                user.getPassword(), user.getWeightStone(), user.getWeightPounds(),
                user.getIdealWeightStone(), user.getIdealWeightPounds(),
                user.getHeight(), user.getBMI(), user.getGender().toString());
    }

    private static final String groupJsonFromat = "{" +
            "\"size\":%d," +
            "\"name\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"invCode\":\"%s\"" +
            "}";
    public static String formatGroupToJson(Group group){
        return String.format(groupJsonFromat, group.getSize(), group.getName(), group.getDescription(), group.getInvCode());
    }

}
