package controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

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
    @FXML
    private Label emptyFieldLabel;

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
    private void registerAccount(ActionEvent event) {
        System.out.println("Register attempted");

        boolean requiredDataEntered = checkRequiredDataEntered();

        if (requiredDataEntered){
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
                    "Password: " + password + "\n" +
                    "Password Verification: " + passwordVerification + "\n" +
                    "Gender: " + gender + "\n" +
                    "Weight: " + stone + " stone and " + pounds + " pounds" + "\n" +
                    "Height: " + height + "cm\n");
        }
        else{
            changeErrorNotificationLabel("Error: Please fill in all the required information");
        }

    }

    private boolean checkRequiredDataEntered(){
        boolean result = true;

        TextField[] textFieldArray = {firstnameText,surnameText,usernameText,emailText,passwordText, passwordVerificationText};

        for (TextField textField : textFieldArray) {
            if (textField.getText().isEmpty()) {
                textField.setStyle("-fx-background-color: #FF4C4C");
                result = false;
            }
        }

        if (genderBox.getValue() == null){
            genderBox.setStyle(("-fx-background-color: #FF4C4C"));
            result=false;
        }
        if (weightStoneSpinner.getValue() == 0 && weightPoundsSpinner.getValue() == 0){
            weightPoundsSpinner.setStyle(("-fx-base: rgba(255, 76, 76, 1)"));
            result=false;
        }
        if (heightSpinner.getValue() == 0){
            heightSpinner.setStyle(("-fx-base: rgba(255, 76, 76, 1)"));
            result=false;
        }

        return result;
    }

    /**
     * This section will change the colour of the input field back to default if some data is input into it.
     * @param event
     */

    @FXML
    public void changeColour(Event event){
        final Node node = (Node) event.getSource();
        final String fieldToChange = node.getId();
        switch(fieldToChange){
            case("firstnameText"):
                firstnameText.setStyle("color: revert");
                break;
            case("surnameText"):
                surnameText.setStyle("color: revert");
                break;
            case("usernameText"):
                usernameText.setStyle("color: revert");
                break;
            case("emailText"):
                emailText.setStyle("color: revert");
                break;
            case("passwordText"):
                passwordText.setStyle("color: revert");
                break;
            case("passwordVerificationText"):
                passwordVerificationText.setStyle("color: revert");
                break;
            case("genderBox"):
                genderBox.setStyle("color: revert");
                break;
            case("weightPoundsSpinner"):
                weightPoundsSpinner.setStyle("color: revert");
                break;
            case("heightSpinner"):
                heightSpinner.setStyle("color: revert");
                break;
        }
    }

    /**
     * This method will be change the label above the register button using the text provided to it.
     */

    private void changeErrorNotificationLabel(String text){
        emptyFieldLabel.setText(text);
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
