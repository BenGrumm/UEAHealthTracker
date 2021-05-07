package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import model.User;
import model.UserDBHelper;

import java.net.URL;
import java.util.ResourceBundle;

public class resetPasswordController extends Controller implements Initializable {

    @FXML
    private TextField detailsTextField;
    @FXML
    private PasswordField passwordTextField, passwordVerificationTextField;
    @FXML
    private Label errorField;
    @FXML
    private Button resetPasswordButton;

    public boolean isEmail(String text) {
        for (char c : text.toCharArray()) {
            if (c == ('@')) {
                return true;
            }
        }
        return false;
    }

    @FXML
    public void resetPassword() {
        String enteredDetails = detailsTextField.getText();
        String enteredPassword = passwordTextField.getText();
        String enteredVerificationPassword = passwordVerificationTextField.getText();

        String correctDetails;

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

            if (registerController.checkValidPassword(enteredPassword) && correctDetails.equals(enteredDetails) &&
                    enteredVerificationPassword.equals(enteredPassword)) {

                userDBHelper.updatePassword(enteredPassword, user.getID());
                successfulPasswordChange();

            } else {
                errorField.setText("Incorrect login. Ensure the details \nentered you entered are" +
                        " correct!");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Tooltip passwordTooltip = new Tooltip("Password must contain number, lowercase \n" +
                "and an uppercase character. Length must \n" +
                "be between 6 and 16 characters.");
        passwordTooltip.setGraphic(new ImageView("/warning_icon.png"));
        passwordTextField.setTooltip(passwordTooltip);

        Tooltip passwordVerificationTooltip = new Tooltip("Passwords must match!");
        passwordVerificationTooltip.setGraphic(new ImageView("/warning_icon.png"));
        passwordVerificationTextField.setTooltip(passwordVerificationTooltip);
    }

}
