package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Group;
import model.GroupDBHelper;
import model.User;
import sample.GUI;

import java.io.IOException;
import java.util.ArrayList;

public class Controller {

    //Global
    @FXML
    public void gotoLogIn(ActionEvent event)
    {

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


