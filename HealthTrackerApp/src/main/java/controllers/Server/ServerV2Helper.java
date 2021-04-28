package controllers.Server;

import model.Group;
import model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ServerV2Helper {

    public static void main(String[] args) {
        ServerV2Helper server = new ServerV2Helper();

        User user1 = new User(0, "Greg", "Grong", "GG", "ben.grummmitt398@gmail.com",
                "e23fkr", 1.5, 4, 5, 0, 0, 0.87f, "MALE");
        User user2 = new User(1, "Pavon", "Preng", "PP", "grummitt3986@gmail.com",
                "e23fkr", 1.5, 4, 5, 0, 0, 0.85f, "MALE");

        Group group1 = new Group(1, "Group 1 Test", "Group desc", 0, "138FEJ9");
        Group group2 = new Group(2, "Group 2 Test", "Group desc", 0, "429WRF");

        server.addUserToServer(user1);
        server.addUserToServer(user2);

        Group g1 = server.createGroup(group1);
        Group g2 = server.createGroup(group2);

        server.joinGroup(user1, g1);
        server.joinGroup(user2, g2);
    }

    public boolean checkIfGroupIDExists(int groupID){
        HTTPJSONResponse response = ServerV2Communication.getGroups();

        if(response == null){
            return false;
        }

        JSONArray arr = response.getBody().getJSONObject("_embedded").getJSONArray("groups");

        for(int i = 0; i < arr.length(); i++){
            if(arr.getJSONObject(i).getInt("resourceId") == groupID){
                return true;
            }
        }

        return false;
    }

    public boolean checkIfGroupInviteExists(String groupInviteCode){
        HTTPJSONResponse response = ServerV2Communication.getGroups();

        if(response == null){
            return false;
        }

        JSONArray arr = response.getBody().getJSONObject("_embedded").getJSONArray("groups");

        for(int i = 0; i < arr.length(); i++){
            if(arr.getJSONObject(i).getString("inviteCode").equals(groupInviteCode)){
                return true;
            }
        }

        return false;
    }

    public boolean checkIfGoalInviteExists(String goalInviteCode){
        HTTPJSONResponse response = ServerV2Communication.getGoals();

        if(response == null){
            return false;
        }

        JSONArray arr = response.getBody().getJSONObject("_embedded").getJSONArray("groups");

        for(int i = 0; i < arr.length(); i++){
            if(arr.getJSONObject(i).getString("inviteCode").equals(goalInviteCode)){
                return true;
            }
        }

        return false;
    }

    public boolean addUserToServer(User user) throws UserAlreadyExists{
        HTTPJSONResponse response = ServerV2Communication.getUsers();

        if(response == null){
            return false;
        }

        JSONArray arr = response.getBody().getJSONObject("_embedded").getJSONArray("users");

        for(int i = 0; i < arr.length(); i++){
            if(arr.getJSONObject(i).getString("resourceId").equals(user.getEmail())){
                throw new UserAlreadyExists(user.getEmail());
            }
        }

        HTTPJSONResponse userResponse = ServerV2Communication.sendUserToServer(user);

        return (userResponse != null) &&
                (userResponse.getResponseCode() == 201 ||
                        (userResponse.getResponseCode() >= 200 && userResponse.getResponseCode() <= 299));
    }

    public Group createGroup(Group group){
        HTTPJSONResponse r = ServerV2Communication.sendGroupToServer(group);

        if(r == null){
            return null;
        }

        return new Group(r.getBody().getInt("resourceId"),
                r.getBody().getString("name"),
                r.getBody().getString("description"),
                r.getBody().getInt("size"),
                r.getBody().getString("invCode"));
    }

    public boolean joinGroup(User user, Group group){

        try {
            HTTPResponse response = ServerV2Communication.joinUserGroup(user.getEmail(), group.getiD());
            return response.getResponseCode() == 204;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean leaveGroup(User user, Group group){

        try {
            return ServerV2Communication.sendDeleteRequest(
                    "/users/" +
                            user.getEmail() +
                            "/groups/" +
                            group.getiD())
                    .getResponseCode()
                    == 204;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}
