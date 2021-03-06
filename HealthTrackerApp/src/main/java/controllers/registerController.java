package controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import model.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

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
    private Spinner<Integer> weightStoneSpinner, weightPoundsSpinner, idealWeightStoneSpinner,
            idealWeightPoundsSpinner;
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

    private final SpinnerValueFactory<Integer> idealWeightStoneSVF =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0,Integer.MAX_VALUE,0,1);

    private final SpinnerValueFactory<Integer> idealWeightPoundsSVF =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0,14,0,1);

    private final SpinnerValueFactory<Double> heightSpinnerSVF =
            new SpinnerValueFactory.DoubleSpinnerValueFactory(0,300,0,0.1);


    /**
     * This method will be called when the user clicks the register button. This event causes the controller to obtain
     * the information that has been entered into the registration form. Once the information has been check it can
     * verify the information entered is valid and all required fields have data entered.
     * @param event Provides information about the event that caused this method to run
     */

    @FXML
    private void registerAccount(ActionEvent event) {

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

            int idealStone = idealWeightStoneSpinner.getValue();
            int idealPounds = idealWeightPoundsSpinner.getValue();

            double height = heightSpinner.getValue();

            boolean validEmail = checkValidEmail(email);
            boolean validPassword = checkValidPassword(password);
            boolean verifyPasswords = verifyPasswords(password, passwordVerification);

            if (validEmail && validPassword && verifyPasswords){
                UserDBHelper userDBHelper = new UserDBHelper();
                GoalDBHelper goalDBHelper = new GoalDBHelper();
                try {
                    boolean added = userDBHelper.addDBUser(firstname,surname,username,email,password,height,stone,
                            pounds,idealStone,idealPounds,gender);

                    if (!added){

                        boolean emailError = userDBHelper.checkValidEmail(email);
                        if (!emailError) {
                            changeErrorNotificationLabel("Error: Email already in use!");
                        }
                        else{
                            changeErrorNotificationLabel("Error: Username already in use!");
                        }
                    }
                    else{
                        accountCreated();
                        WeightDBHelper weightDBHelper = new WeightDBHelper();
                        UserWeight userWeight = new UserWeight(stone, pounds);
                        weightDBHelper.addWeight(userWeight, userDBHelper.getUserViaEmail(email).getID());
                    }

                    //George code for goals stuff... :)
                    //Generate weight goal based off information entered.
                    //Give a week to complete and name it 'Getting started'
                    //Here is the code below smiley face ::))
                    goalDBHelper.addGoal("Daily calories",
                            Goal.goal.DIET,
                            0,
                            2000,
                            String.valueOf(userDBHelper.getUserViaUsername(username).getID()),
                            0);

                    goalDBHelper.addGoal("Getting started",
                            Goal.goal.WEIGHT,
                            stone+(pounds/14),
                            idealStone+(idealPounds/14),
                            LocalDate.now(),
                            LocalDate.now().plusWeeks(1),
                            String.valueOf(userDBHelper.getUserViaUsername(username).getID()),
                            0);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            else if (!validEmail){
                changeErrorNotificationLabel("Error: Please enter valid email address (example@example.com)");
            }
            else if (!validPassword){
                changeErrorNotificationLabel("Error: Please enter a valid password \n" +
                        "(Hover over field for more info)");
            }
            else{
                changeErrorNotificationLabel("Error: Please ensure that the passwords match");
            }

        }
        else{
            changeErrorNotificationLabel("Error: Please fill in all the required information");
        }

    }

    private boolean verifyPasswords(String password, String passwordVerification) {
        return password.equals(passwordVerification);
    }

    public static boolean checkValidPassword(String password) {
        int minPasswordLength = 6;
        int maxPasswordLength = 16;
        if (password.length() > maxPasswordLength || password.length() < minPasswordLength){
            return false;
        }

        boolean containsInteger = false;
        boolean containsLowercaseCharacter = false;
        boolean containsUppercaseCharacter = false;

        List<Character> intList = Arrays.asList('0','1','2','3','4','5','6','7','8','9');
        List<Character> lowercaseCharList = Arrays.asList('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
                                            'p','q','r','s','t','u','v','w','x','y','z');

        List<Character> uppercaseCharList = new ArrayList<>(Collections.emptyList());
        for (char c : lowercaseCharList){
            uppercaseCharList.add(Character.toUpperCase(c));
        }

        for (char c : password.toCharArray()){
            if (intList.contains(c)){
                containsInteger = true;
            }
            else if (lowercaseCharList.contains(c)){
                containsLowercaseCharacter = true;
            }
            else if (uppercaseCharList.contains(c)){
                containsUppercaseCharacter = true;
            }
        }
        return containsInteger && containsLowercaseCharacter && containsUppercaseCharacter;
    }

    private boolean checkValidEmail(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    /**
     * This section will check that all the required fields has data entered into them. This method will also change
     * the colour of the field to red if the field is empty to indicate to the user that it is required to enter data.
     * @return True if all fields have information or false if one or more field is empty.
     */

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
        if (idealWeightStoneSpinner.getValue() == 0 && idealWeightPoundsSpinner.getValue() == 0){
            idealWeightPoundsSpinner.setStyle(("-fx-base: rgba(255, 76, 76, 1)"));
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
     * @param event Provided information about the event that caused this method to run.
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
            case("idealWeightPoundsSpinner"):
                idealWeightPoundsSpinner.setStyle("color: revert");
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
     * @param url Relative path for the root object.
     * @param resourceBundle Resources used to localise the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        weightStoneSpinner.setValueFactory(weightStoneSVF);
        weightPoundsSpinner.setValueFactory(weightPoundsSVF);
        idealWeightStoneSpinner.setValueFactory(idealWeightStoneSVF);
        idealWeightPoundsSpinner.setValueFactory(idealWeightPoundsSVF);
        heightSpinner.setValueFactory(heightSpinnerSVF);
        ObservableList<String> genders = FXCollections.observableArrayList("Male", "Female");
        genderBox.setItems(genders);

        Tooltip passwordTooltip = new Tooltip("Password must contain number, lowercase \n" +
                "and an uppercase character. Length must \n" +
                "be between 6 and 16 characters.");
        passwordTooltip.setGraphic(new ImageView("/warning_icon.png"));
        passwordText.setTooltip(passwordTooltip);

        Tooltip passwordVerificationTooltip = new Tooltip("Passwords must match!");
        passwordVerificationTooltip.setGraphic(new ImageView("/warning_icon.png"));
        passwordVerificationText.setTooltip(passwordVerificationTooltip);
    }

}
