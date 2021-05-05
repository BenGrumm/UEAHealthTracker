package controllers;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.User;
import model.UserDBHelper;

import java.net.URL;
import java.util.*;

public class logInController extends Controller{

    @FXML
    private TextField detailsTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label errorField;
    @FXML
    private Button loginButton;

    public boolean isEmail(String text) {
        for (char c : text.toCharArray()) {
            if (c == ('@')) {
                return true;
            }
        }
        return false;
    }

    @FXML
    public void login() {
        String enteredDetails = detailsTextField.getText();
        String enteredPassword = passwordTextField.getText();

        String correctDetails;
        String correctPassword;

        boolean isEmail = isEmail(enteredDetails);
        UserDBHelper userDBHelper = new UserDBHelper();
        User user;
        if (isEmail) {
            user = userDBHelper.getUserViaEmail(enteredDetails);
        } else {
            user = userDBHelper.getUserViaUsername(enteredDetails);
        }

        if (user == null) {
            errorField.setText("Incorrect login. Ensure the details \nentered you entered are" +
                    " correct!");
        } else {
            if (isEmail) {
                correctDetails = user.getEmail();
            } else {
                correctDetails = user.getUsername();
            }

            correctPassword = user.getPassword();
            if (correctDetails.equals(enteredDetails) && correctPassword.equals(enteredPassword)) {
                User.loggedIn = user;
                User.setDailyCalorieLimit();
                logged_in();
            } else {
                errorField.setText("Incorrect login. Ensure the details entered you entered are" +
                        " correct or register an account!");
            }
        }
    }
}
