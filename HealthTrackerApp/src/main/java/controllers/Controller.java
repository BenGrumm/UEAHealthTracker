package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import model.GroupDBHelper;
import model.User;
import sample.GUI;

import java.io.IOException;
import java.util.ArrayList;

public class Controller {

    @FXML
    Button main_menu;

    @FXML
    Button log_in;

    @FXML
    Button diet;

    @FXML
    Button groups;

    @FXML
    Button goals;

    @FXML
    public void navigatePage(ActionEvent event)
    {
        String id = ((Control) event.getSource()).getId();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + id + ".fxml"));
        try {
            Parent root = loader.load();
            GUI.changeScene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void successfulPasswordChange()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/successful_password_change.fxml"));
        try {
            Parent root = loader.load();
            GUI.changeScene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logged_in(){

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
        try {
            Parent root = loader.load();
            GUI.changeScene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void accountCreated(){

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/successful_account_creation.fxml"));
        try {
            Parent root = loader.load();
            GUI.changeScene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to load group section. Uses logged in users information to load the relevent scene.
     */
    public void goToGroupPage(){
        User currentUser = User.getLoggedIn();
        GroupDBHelper GDBH= new GroupDBHelper();

        ArrayList<Integer> usersGroupIDs = new ArrayList<Integer>();
        usersGroupIDs = GDBH.getUsersGroupIDs(currentUser.getID());

        FXMLLoader loader;
        Parent root = null;

        if(!(usersGroupIDs.size() == 0)) {
            loader = new FXMLLoader(getClass().getResource("/groups.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            groupController gc = loader.getController();
            gc.SetUpGroupHomepage();

        }
        else{
            loader = new FXMLLoader(getClass().getResource("/no_group_landing_page.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        GUI.changeScene(root);
    }

}


