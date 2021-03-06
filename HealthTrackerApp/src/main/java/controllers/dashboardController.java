package controllers;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import model.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import javafx.event.ActionEvent;

/**
 * This class is the controller for the registration from the main_menu UI. It works by setting storing all the
 * information that is entered by the user and then sends it to the relevant method for verification if required.
 * @author Harry Burns
 */

public class dashboardController extends Controller implements Initializable {

    /**
     * This section sets out the required parameters.
     */
    @FXML
    Label bmiLabel, bmiClassification, showWeightStoneLabel, showWeightPoundsLabel, caloriesRemaining,
            targetWeightStoneLabel, targetWeightPoundsLabel, targetBmiClassification, targetBmiLabel, nameLabel, dailyCalories;
    @FXML
    Spinner<Integer> weightPoundsSpinner, weightStoneSpinner;

    @FXML
    DatePicker datePicker;

    private final SpinnerValueFactory<Integer> weightStoneSVF =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0,Integer.MAX_VALUE,User.loggedIn.getWeightStone(),1);

    private final SpinnerValueFactory<Integer> weightPoundsSVF =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0,14,User.loggedIn.getWeightPounds(),1);

    @FXML
    public void updateWeight(ActionEvent event){
        LocalDate tomorrow = LocalDate.now().plus(1, ChronoUnit.DAYS);
        if (!(weightPoundsSpinner.getValue() == 0 && weightStoneSpinner.getValue() == 0)) {
            if (datePicker.getValue() != null && datePicker.getValue().isBefore(tomorrow)) {

                UserWeight userWeight = new UserWeight(weightStoneSpinner.getValue(), weightPoundsSpinner.getValue(), datePicker.getValue());
                WeightDBHelper weightDBHelper = new WeightDBHelper();
                UserDBHelper userDBHelper = new UserDBHelper();

                weightDBHelper.addWeight(userWeight, User.getLoggedIn().getID());

                userDBHelper.updateWeight(userWeight.getStones(), userWeight.getPounds());

                System.out.println(LocalDate.now());
                System.out.println(datePicker.getValue());
                if ((datePicker.getValue().isEqual(LocalDate.now()))){
                    User.getLoggedIn().setWeightStone(userWeight.getStones());
                    User.getLoggedIn().setWeightPounds(userWeight.getPounds());

                    User.getLoggedIn().setBMI(User.calculateBMI(User.loggedIn.getHeight(),
                            userWeight.getStones(), userWeight.getPounds()));
                }

                GoalDBHelper goalDBHelper = new GoalDBHelper();
                ArrayList<Goal> goals = goalDBHelper.getGoalsByUserId(User.getLoggedIn().getID());
                for(Goal goal: goals) {
                    if (goal.getGoalType() == Goal.goal.WEIGHT) {
                        //float currentProgress = goal.getProgress();
                        goalDBHelper.updateGoalProgress(goal.getGoalID(), (float)User.loggedIn.getWeightKG());
                    }
                }

                setBMI();
            }
            else{
                datePicker.setStyle(("-fx-base: rgba(255, 76, 76, 1)"));
            }
        }
        else{
            weightPoundsSpinner.setStyle(("-fx-base: rgba(255, 76, 76, 1)"));
        }
    }

    public void changeColour(Event event) {
        if ("weightPoundsSpinner".equals(((Control) event.getSource()).getId())) {
            weightPoundsSpinner.setStyle("color: revert");
        } else {
            datePicker.setStyle("color: revert");
        }
    }


    public void setBMI(){
        double bmi = Math.round(User.loggedIn.getBMI() * 10) / 10.0;;

        bmiLabel.setText(Double.toString(bmi));

        if (bmi <= 18.5) {
            bmiClassification.setText("Underweight");
        } else if (18.6 <= bmi && bmi <= 24.9) {
            bmiClassification.setText("Healthy weight");
        } else if (25 <= bmi && bmi <= 29.9) {
            bmiClassification.setText("Overweight");
        } else {
            bmiClassification.setText("Obese");
        }

        double targetBmi = Math.round(User.calculateBMI(User.loggedIn.getHeight(),User.loggedIn.getIdealWeightStone(),
                User.loggedIn.getIdealWeightPounds()) * 10) / 10.0;;
        targetBmiLabel.setText(Double.toString(targetBmi));
        if (targetBmi <= 18.5) {
            targetBmiClassification.setText("Underweight");
        } else if (18.6 <= targetBmi && targetBmi <= 24.9) {
            targetBmiClassification.setText("Healthy weight");
        } else if (25 <= targetBmi && targetBmi <= 29.9) {
            targetBmiClassification.setText("Overweight");
        } else {
            targetBmiClassification.setText("Obese");
        }

        showWeightStoneLabel.setText(Integer.toString(User.getLoggedIn().getWeightStone()));
        showWeightPoundsLabel.setText(Integer.toString(User.getLoggedIn().getWeightPounds()));

        targetWeightStoneLabel.setText(Integer.toString(User.getLoggedIn().getIdealWeightStone()));
        targetWeightPoundsLabel.setText(Integer.toString(User.getLoggedIn().getIdealWeightPounds()));

        datePicker.setValue(LocalDate.now());
    }


    /**
     * This method will run when the details page is loaded. It sets the values for the BMI.
     * @param url Relative path for the root object.
     * @param resourceBundle Resources used to localise the root object.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setBMI();
        GoalDBHelper goalDBHelper = new GoalDBHelper();
        nameLabel.setText(User.loggedIn.getFirstName());

        weightStoneSpinner.setValueFactory(weightStoneSVF);
        weightPoundsSpinner.setValueFactory(weightPoundsSVF);
        ArrayList<Goal> goals = goalDBHelper.getGoalsByUserId(User.getLoggedIn().getID());
        for(Goal goal: goals) {
            if (goal.getGoalType() == Goal.goal.DIET) {
                float currentProgress = goal.getProgress();
                User.dailyCalories = (int)currentProgress;
            }
        }
        dailyCalories.setText(String.valueOf(User.dailyCalorieLimit - User.dailyCalories));
        //dailyCalories.setText(goalDBHelper.getGoal(__USERID__).getTarget()-goalDBHelper.getGoal(__USERID__).getProgress());

    }
}
