package controllers.Server;

import model.User;

import java.util.Base64;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ServerCommunication {

    private static final String baseURL = "http://localhost:8080";

    public static void main(String[] args) {
        System.out.println(addUser(new User("Ben", "Grummit", "bgrumm", "testEmail@gmail.com",
                "pworded", 1.9, 120, 49, "MALE")));
    }

    private static final String userJsonFormat =
            "{\"email\":\"%s\",\"firstName\":\"%s\",\"surname\":\"%s\"," +
            "\"username\":\"%s\",\"password\":\"%s\",\"weightStone\":%d," +
            "\"weightPounds\":%d,\"height\":%f,\"gender\":\"%s\"}";
    public static String formatUserToJson(User user){
        return String.format(userJsonFormat, user.getEmail(), user.getFirstName(), user.getSurname(), user.getUsername(),
                user.getPassword(), user.getWeightStone(), user.getWeightPounds(), user.getHeight(), user.getGender().toString());
    }

    private static void addAuthToRequest(HttpURLConnection con){
        String auth = "admin:password";
        String authHeader = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        con.setRequestProperty("Authorization", authHeader);
    }

    public static String addUser(User user){
        String jsonComplete = formatUserToJson(user);

        try {
            return sendPostRequestWithJson("/users", jsonComplete);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String sendPostRequestWithJson(String relativeURLExtension, String json) throws IOException {
        URL url = new URL(baseURL + relativeURLExtension);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        addAuthToRequest(con);

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int rc = con.getResponseCode();
        System.out.println("Response code = " + rc);

        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }

    }

    private static String sendGetRequest(){
        return "";
    }

}
