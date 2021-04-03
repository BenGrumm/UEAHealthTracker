package controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller for the registration from the main_menu UI. It works by setting storing all the
 * information that is entered by the user and then sends it to the relevant method for verification if required.
 * @author Harry Burns
 */

public class registerController extends Controller implements Initializable {

    /**
     * This section sets out the required parameters.
     */

    @FXML
    private TextField firstnameText, surnameText, usernameText, emailText, passwordText, passwordVerificationText;
    @FXML
    private Spinner<Integer> weightStoneSpinner, weightPoundsSpinner;
    @FXML
    private Spinner<Double> heightSpinner;
    @FXML
    private ChoiceBox<String> genderBox;

    private final SpinnerValueFactory<Integer> weightStoneSVF =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0,Integer.MAX_VALUE,0,1);

    private final SpinnerValueFactory<Integer> weightPoundsSVF =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0,14,0,1);

    private final SpinnerValueFactory<Double> heightSpinnerSVF =
            new SpinnerValueFactory.DoubleSpinnerValueFactory(0,300,0,0.1);


    /**
     * This method will be called when the user clicks the register button. This event causes the controller to obtain
     * the information that has been entered into the registration form. Once the information has been check it can
     * verify the information entered is valid and all required fields have data entered.
     * @param event
     */

    @FXML
    public void registerAccount(ActionEvent event) {
        System.out.println("Register attempted");

        String firstname = firstnameText.getText();
        String surname = surnameText.getText();
        String username = usernameText.getText();
        String email = emailText.getText();
        String password = passwordText.getText();
        String passwordVerification = passwordVerificationText.getText();
        String gender = genderBox.getValue();

        int stone = weightStoneSpinner.getValue();
        int pounds = weightPoundsSpinner.getValue();

        double height = heightSpinner.getValue();

        System.out.println("Firstname: " + firstname + "\n" +
                           "Surname: " + surname + "\n" +
                           "Username: " + username + "\n" +
                           "Email: " + email + "\n" +
                           "Password: " + password  + "\n" +
                           "Password Verification: " + passwordVerification + "\n" +
                           "Gender: " + gender + "\n" +
                           "Weight: " + stone + " stone and " + pounds + " pounds" + "\n" +
                           "Height: " + height + "cm\n");

    }

    /**
     * This method will run when the main_menu is loaded. It sets the values and limits of the spinners as well as
     * setting the value for the drop down menu of the gender option.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        weightStoneSpinner.setValueFactory(weightStoneSVF);
        weightPoundsSpinner.setValueFactory(weightPoundsSVF);
        heightSpinner.setValueFactory(heightSpinnerSVF);
        ObservableList<String> genders = FXCollections.observableArrayList("Male", "Female");
        genderBox.setItems(genders);
    }

}
