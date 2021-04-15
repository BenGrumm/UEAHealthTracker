package controllers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.User;
import model.UserDBHelper;

import java.net.URL;
import java.util.*;

public class logInController extends Controller implements Initializable {

    @FXML
    private TextField detailsTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label errorField;
    @FXML
    private Button loginButton;

    public boolean isEmail(String text){
        for (char c : text.toCharArray()){
            if (c == ('@')){
                return true;
            }
        }
        return false;
    }

    @FXML
    public void login(){
        String enteredDetails = detailsTextField.getText();
        String enteredPassword = passwordTextField.getText();

        String correctDetails;
        String correctPassword;

        boolean isEmail = isEmail(enteredDetails);
        UserDBHelper userDBHelper = new UserDBHelper();
        User user;
        if (isEmail){
            user = userDBHelper.getUserViaEmail(enteredDetails);
        }
        else{
            user = userDBHelper.getUserViaUsername(enteredDetails);
        }

        if (user == null){
            errorField.setText("Incorrect login - Null returned");
        }
        else {
            if (isEmail){
                correctDetails = user.getEmail();
            }
            else{
                correctDetails = user.getUsername();
            }

            correctPassword = user.getPassword();
            if (correctDetails.equals(enteredDetails) && correctPassword.equals(enteredPassword)) {

                //Redirect to next page - Store user logged in somewhere
                User.loggedIn = user;
                System.out.println("SUCCESSFUL LOGIN");

                System.out.println(User.getLoggedIn());

            } else {
                errorField.setText("Incorrect login");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("yajr");
    }
}
