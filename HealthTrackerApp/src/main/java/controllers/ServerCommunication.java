package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServerCommunication {

    public static void main(String[] args) {
        try {
            URL url = new URL("http://localhost:8080/users");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");

            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");

            con.setDoOutput(true);

            String jsonData = "{\"email\":\"testNum2@gmail.com\",\"firstName\":\"Ben\",\"surname\":\"Grummitt\",\"username\":\"bgrumTesting\",\"password\":\"pword\",\"weightStone\":0,\"weightPounds\":0,\"height\":1.94,\"gender\":\"MALE\"}";

            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonData.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
