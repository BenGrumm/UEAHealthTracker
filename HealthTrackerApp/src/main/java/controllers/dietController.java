package controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class dietController extends Controller implements Initializable {

    @FXML
    private TextField foodNameText, caloriesConsumedText, exerciseNameText, durationText, caloriesBurnedText;
    @FXML
    private ChoiceBox<String> mealChoiceBox;
    @FXML
    private DatePicker foodDatePicker, exerciseDatePicker;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> meals = FXCollections.observableArrayList("Breakfast", "Lunch", "Dinner", "Snack");
        mealChoiceBox.setItems(meals);

    }

    public void createDietEntry(javafx.event.ActionEvent actionEvent) {
        String foodName = foodNameText.getText();
        String caloriesConsumed = caloriesConsumedText.getText();
        String mealChoice = mealChoiceBox.getValue();
        LocalDate foodDate = foodDatePicker.getValue();

        System.out.println("Food: " + foodName + "\n" +
                "Calories consumed: " + caloriesConsumed + "\n" +
                "Meal: " + mealChoice + "\n" +
                "Date consumed: " + foodDate);
    }

    public void createExerciseEntry(javafx.event.ActionEvent actionEvent) {
        String exerciseName = exerciseNameText.getText();
        String duration = durationText.getText();
        String caloriesBurned = caloriesBurnedText.getText();
        LocalDate exerciseDate = exerciseDatePicker.getValue();

        System.out.println("Exercise: " + exerciseName + "\n" +
                "Duration: " + duration + "\n" +
                "Calories Burned: " + caloriesBurned + "\n" +
                "Date exercised: " + exerciseDate);
    }

    /*
    to do:
    validate
    save to database when save button pressed
    allow for selection from predefined list of exercises
     */
}
