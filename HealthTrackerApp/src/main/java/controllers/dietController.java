package controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import model.Exercise;
import model.ExerciseType;
import model.Food;
import model.ExerciseTypeDBHelper;
import model.ExerciseDBHelper;

public class dietController extends Controller implements Initializable {

    private ExerciseTypeDBHelper db = new ExerciseTypeDBHelper();
    private ExerciseDBHelper exdb = new ExerciseDBHelper();
    @FXML
    private TextField foodNameText, caloriesConsumedText, exerciseNameText, durationText, caloriesBurnedText,
            exerciseSearchDurationText;
    @FXML
    private ChoiceBox<String> mealChoiceBox;
    @FXML
    private ComboBox<ExerciseType> exerciseSearchCombo;
    @FXML
    private DatePicker foodDatePicker, exerciseDatePicker;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> meals = FXCollections.observableArrayList("Breakfast", "Lunch", "Dinner", "Snack");
        mealChoiceBox.setItems(meals);
        /*
        To allow user to search through already set exercises, populate a combo box with objects from
        exercise type database
         */
        exerciseSearchCombo.setItems(FXCollections.observableArrayList(db.getAllExercises()));
    }

    public void createDietEntry(javafx.event.ActionEvent actionEvent) {
        String foodName = foodNameText.getText();
        String caloriesConsumed = caloriesConsumedText.getText();
        String mealChoice = mealChoiceBox.getValue();
        LocalDate foodDate = foodDatePicker.getValue();

        // Currently testing data can be read in from GUI
        System.out.println("Food: " + foodName + "\n" +
                "Calories consumed: " + caloriesConsumed + "\n" +
                "Meal: " + mealChoice + "\n" +
                "Date consumed: " + foodDate);
        Food foodEntry = new Food(foodName, Integer.parseInt(caloriesConsumed), mealChoice, foodDate);
    }

    public void createExerciseEntry(javafx.event.ActionEvent actionEvent) {
        //Placeholder exercise ID for custom exercise entry
        int exerciseId = 500;
        // retrieving values from either custom or preset exercise entry
        ExerciseType type = exerciseSearchCombo.getValue();
        String exerciseName = exerciseNameText.getText();
        Double duration = Double.parseDouble(durationText.getText());
        Double caloriesBurned = Double.parseDouble(caloriesBurnedText.getText());
        LocalDate exerciseDate = exerciseDatePicker.getValue();

        System.out.println("Exercise: " + exerciseName + "\n" +
                "Duration: " + duration + "\n" +
                "Calories Burned: " + caloriesBurned + "\n" +
                "Date exercised: " + exerciseDate);

        Exercise exerciseEntry = new Exercise(exerciseId, duration, caloriesBurned, type, exerciseDate);
        exdb.addExerciseToDB(exerciseEntry);

    }

    public void fillInSearchEntry(javafx.event.ActionEvent actionEvent){
        // NEEDS ERROR HANDLING FOR IF NOTHING HAS BEEN SELECTED
        ExerciseType entry = exerciseSearchCombo.getValue();
        exerciseNameText.setText(entry.getExerciseDescription());
        durationText.setText(exerciseSearchDurationText.getText());
        // REAL WEIGHT SHOULD COME FROM THE USER WHEN THEY ARE LOGGED IN
        int testWeightKG = 100;
        // Need to check the below maths is right? Result doesnt seem like the right number
        float caloriesBurnedConversion = (entry.getCaloriesBurned()/60)*testWeightKG*Integer.parseInt(exerciseSearchDurationText.getText());
        caloriesBurnedText.setText(Float.toString(caloriesBurnedConversion));
    }

    /*
    to do:
    create food type class and food database helper and food type database helper class
    create predefined database of food and exercise types
    validate
    - check when entering food or exercise that an exercise of the same name doesnt already exist in the database
        if it does, suggest the user pick from the list instead (same for food) to stop lots of duplicates
        - also, if food or exercise name not already exist in the database then add it for next time
    save to database when save button pressed
    allow for selection from predefined list of exercises
     */
}
