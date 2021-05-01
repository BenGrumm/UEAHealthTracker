package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.User;

import java.net.URL;
import java.util.*;

/**
 * This class is the controller for the registration from the main_menu UI. It works by setting storing all the
 * information that is entered by the user and then sends it to the relevant method for verification if required.
 * @author Harry Burns
 */

public class detailsController extends Controller implements Initializable {

    /**
     * This section sets out the required parameters.
     */

    @FXML
    Label bmiLabel, bmiClassification, showWeightStoneLabel, showWeightPoundsLabel, caloriesRemaining,
            targetWeightStoneLabel, targetWeightPoundsLabel, targetBmiClassification, targetBmiLabel, nameLabel;

    /**
     * This method will run when the details page is loaded. It sets the values for the BMI.
     * @param url Relative path for the root object.
     * @param resourceBundle Resources used to localise the root object.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        double bmi = Math.round(User.loggedIn.getBMI() * 10) / 10.0;;
        bmiLabel.setText(Double.toString(bmi));
        if (bmi <= 18.5) {
            bmiClassification.setText("Underweight");
        } else if (18.6 <= bmi && bmi <= 24.9) {
            bmiClassification.setText("Healthy weight");
        } else if (25 <= bmi && bmi <= 29.9) {
            bmiClassification.setText("Overweight");
        } else {
            bmiClassification.setText("Obesity");
        }

        double targetBmi = Math.round(User.calculateBMI(User.loggedIn.getHeight(),User.loggedIn.getIdealWeightStone(),
                User.loggedIn.getIdealWeightPounds()) * 10) / 10.0;;
        targetBmiLabel.setText(Double.toString(targetBmi));
        if (bmi <= 18.5) {
            targetBmiClassification.setText("Underweight");
        } else if (18.6 <= bmi && bmi <= 24.9) {
            targetBmiClassification.setText("Healthy weight");
        } else if (25 <= bmi && bmi <= 29.9) {
            targetBmiClassification.setText("Overweight");
        } else {
            targetBmiClassification.setText("Obese");
        }

        showWeightStoneLabel.setText(Integer.toString(User.getLoggedIn().getWeightStone()));
        showWeightPoundsLabel.setText(Integer.toString(User.getLoggedIn().getWeightPounds()));

        targetWeightStoneLabel.setText(Integer.toString(User.getLoggedIn().getIdealWeightStone()));
        targetWeightPoundsLabel.setText(Integer.toString(User.getLoggedIn().getIdealWeightPounds()));

        nameLabel.setText(User.loggedIn.getFirstName());

    }
}
