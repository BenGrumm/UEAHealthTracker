package controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import model.*;

public class dietController extends Controller implements Initializable {

    private ExerciseTypeDBHelper exerciseTypeDB = new ExerciseTypeDBHelper();
    private ExerciseDBHelper exerciseDB = new ExerciseDBHelper();
    private FoodTypeDBHelper foodTypeDB = new FoodTypeDBHelper();
    private FoodDBHelper foodDB = new FoodDBHelper();
    @FXML
    private TextField foodNameText, caloriesConsumedText, servingSizeText, dietSearchServingSizeText, exerciseNameText,
            durationText, caloriesBurnedText, exerciseSearchDurationText;
    @FXML
    private ChoiceBox<String> mealChoiceBox;
    @FXML
    private ComboBox<ExerciseType> exerciseSearchCombo;
    @FXML
    private ComboBox<FoodType> dietSearchCombo;
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
        exerciseSearchCombo.setItems(FXCollections.observableArrayList(exerciseTypeDB.getAllExercises()));
        /*
        To allow user to search through already set foods, populate a combo box with objects from
        food type database
         */
        dietSearchCombo.setItems(FXCollections.observableArrayList(foodTypeDB.getAllFoods()));

    }

    /**
     * Function to trigger exercise data validation, then call functions to create exercise entry either custom or from
     * predefined list
     * @param actionEvent action event, save exercise entry button pressed
     */
    public void createExerciseEntry(javafx.event.ActionEvent actionEvent) {
        boolean exerciseDataEntered = checkExerciseDataEntered();
        boolean dateValid = checkDateValid(exerciseDatePicker.getValue());
        if(exerciseDataEntered && dateValid){
            // if exercise name in exercise type
            if(exerciseTypeDB.checkTypeExists(exerciseNameText.getText())){
                createExerciseEntryFromList();
            }
            /* if exercise name not already in exercise type then creating custom exercise therefore need extra
            functionality to create an exercise type object as well as an exericse object
             */
            else{
                createCustomExerciseEntry();
            }
        }
    }

    /**
     * Function to create an exercise object which already has a corresponding exercise type object available from
     * the database. Then adds this object to the users exercises table.
     */
    private void createExerciseEntryFromList(){
        //Placeholder exercise ID for custom exercise entry - needs to be number of entries in user exercises db?
        int exerciseId = 500;
        // retrieving values from either custom or preset exercise entry
        ExerciseType type = exerciseSearchCombo.getValue();
        String exerciseName = exerciseNameText.getText();
        double duration = Double.parseDouble(durationText.getText());
        double caloriesBurned = Double.parseDouble(caloriesBurnedText.getText());
        LocalDate exerciseDate = exerciseDatePicker.getValue();

        System.out.println("Exercise: " + exerciseName + "\n" +
                "Duration: " + duration + "\n" +
                "Calories Burned: " + caloriesBurned + "\n" +
                "Date exercised: " + exerciseDate);

        Exercise exerciseEntry = new Exercise(exerciseId, duration, caloriesBurned, type, exerciseDate);
        exerciseDB.addExerciseToDB(exerciseEntry);
    }

    /**
     * Function to create an exercise object which does not already have a corresponding exercise type object available
     * from the database. Therefore creates an exercise type object, then exercise object, then adds these objects to
     * the exercise types table and the users exercises table respectively.
     */
    public void createCustomExerciseEntry(){
        // exercise type id needs to be number of items in exercise type database
        int typeId = exerciseTypeDB.exerciseTypeTableLength() + 1;
        float caloriesBurned = Float.parseFloat(caloriesBurnedText.getText());
        int testWeightKG = 100;
        float durationInHours = Float.parseFloat(durationText.getText());
        float calsPerKGPerHour = caloriesBurned / testWeightKG / durationInHours;
        ExerciseType type = new ExerciseType(typeId, exerciseNameText.getText(), calsPerKGPerHour);
        exerciseTypeDB.addExerciseTypeToDB(type);
        System.out.println(type.toString());
        int exerciseId = exerciseDB.exerciseTableLength() + 1;
        Exercise customExercise = new Exercise(exerciseId, Float.parseFloat(durationText.getText()),
                Float.parseFloat(caloriesBurnedText.getText()), type, exerciseDatePicker.getValue());
        exerciseDB.addExerciseToDB(customExercise);
    }

    /**
     * Function takes data from the user's selected exercise type and uses it to fill in the form data to then be saved
     * @param actionEvent, confirm search button pressed
     */
    public void fillInSearchEntry(javafx.event.ActionEvent actionEvent){
        boolean searchExerciseDataEntered = checkSearchExerciseDataEntered();
        if(searchExerciseDataEntered){
            ExerciseType entry = exerciseSearchCombo.getValue();
            exerciseNameText.setText(entry.getExerciseDescription());
            durationText.setText(exerciseSearchDurationText.getText());
            // REAL WEIGHT SHOULD COME FROM THE USER WHEN THEY ARE LOGGED IN
            int testWeightKG = 100;
            // Need to check the below maths is right? Result doesnt seem like the right number
            float caloriesBurnedConversion = (entry.getCaloriesBurned()/60)*testWeightKG*Integer.parseInt(exerciseSearchDurationText.getText());
            caloriesBurnedText.setText(Float.toString(caloriesBurnedConversion));
        }
    }

    /**
     * Function to check if exercise data capture tools in the GUI have been filled in. If not, changes the boxes colour
     * @return boolean - true if data entered, false if any empty
     */
    private boolean checkExerciseDataEntered(){
        boolean result = true;
        TextField[] textFields = {exerciseNameText, durationText, caloriesBurnedText};
        for(TextField tf : textFields){
            if(tf.getText().isEmpty()) {
                tf.setStyle("-fx-background-color: #FF4C4C");
                result = false;
            }
        }
        if(exerciseDatePicker.getValue() == null){
            exerciseDatePicker.setStyle(("-fx-background-color: #FF4C4C"));
            result=false;
        }
        if(exerciseSearchCombo.getValue() != null){
            if(exerciseSearchDurationText.getText().isEmpty()){
                exerciseSearchDurationText.setStyle("-fx-background-color: #FF4C4C");
                result = false;
            }
        }
        return result;
    }

    /**
     * Function to check that, if the user has pressed the button to select a preset list of exercises, they have filled
     * in the right data capture boxes in the GUI. If not, boxes change colour
     * @return boolean - true if data entered, false if any empty
     */
    private boolean checkSearchExerciseDataEntered(){
        boolean result = true;
        if(exerciseSearchCombo.getValue() == null){
            exerciseSearchCombo.setStyle(("-fx-background-color: #FF4C4C"));
            result = false;
        }
        if(exerciseSearchDurationText.getText().isEmpty()){
            exerciseSearchDurationText.setStyle("-fx-background-color: #FF4C4C");
            result = false;
        }
        return result;
    }

    /**
     * Function to change GUI data capture boxes colours back to default colours, if they had been changed for not
     * having data in them but now do.
     * @param event - triggered if data capture boxes now have data in them
     */
    @FXML
    public void changeExerciseColour(Event event){
        final Node node = (Node) event.getSource();
        final String fieldToChange = node.getId();
        switch(fieldToChange){
            case("exerciseNameText"):
                exerciseNameText.setStyle("color: revert");
                break;
            case("durationText"):
                durationText.setStyle("color: revert");
                break;
            case("caloriesBurnedText"):
                caloriesBurnedText.setStyle("color: revert");
                break;
            case("exerciseSearchDurationText"):
                exerciseSearchDurationText.setStyle("color: revert");
                break;
            case("exerciseDatePicker"):
                exerciseDatePicker.setStyle("color: revert");
                break;
            case("exerciseSearchCombo"):
                exerciseSearchCombo.setStyle("color: revert");
                break;
        }
    }

    /**
     * Function to check that date entered is in the past or present - not future
     * @return boolean - true if date is valid, false otherwise
     */
    private boolean checkDateValid(LocalDate date){
        boolean result = true;
        if(date.isAfter(LocalDate.now())){
            result = false;
        }
        return result;
    }

    /**
     * Function to trigger diet data validation, then call functions to create a diet entry either custom or from
     * predefined list
     * @param actionEvent - save diet entry button pressed
     */
    public void createDietEntry(javafx.event.ActionEvent actionEvent) {
        boolean foodDataEntered = checkDietDataEntered();
        boolean dateValid = checkDateValid(foodDatePicker.getValue());
        if(foodDataEntered && dateValid) {
            if (foodTypeDB.checkTypeExists(foodNameText.getText())) {
                createDietEntryFromList();
            } else {
                createCustomDietEntry();
            }
        }
    }

    /**
     * Function to create a food object which already has a corresponding food type object available from
     * the database. Then adds this object to the users foods table.
     */
    public void createDietEntryFromList(){
        //Placeholder food ID for custom food entry - needs to be number of entries in user foods db? same issue as with exercises
        int foodId = 500;
        // retrieving values from either custom or preset exercise entry
        FoodType type = dietSearchCombo.getValue();
        String foodName = foodNameText.getText();
        double calories = Double.parseDouble(caloriesConsumedText.getText());
        String meal = mealChoiceBox.getValue();
        double servingSize = Double.parseDouble(servingSizeText.getText());
        LocalDate date = foodDatePicker.getValue();
        Food dietEntry = new Food(foodId, foodName, calories, meal, servingSize, date, type);
        foodDB.addFoodToDB(dietEntry);
    }

    /**
     * Function to create a food object which does not already have a corresponding food type object available
     * from the database. Therefore creates a food type object, then food object, then adds these objects to
     * the food types table and the users foods table respectively.
     */
    public void createCustomDietEntry(){
        int typeId = foodTypeDB.foodTypeTableLength()+1;
        double caloriesPer100g = 100 / Double.parseDouble(servingSizeText.getText()) *
                Double.parseDouble(caloriesConsumedText.getText()) ;
        FoodType type = new FoodType(typeId, foodNameText.getText(), caloriesPer100g);
        foodTypeDB.addFoodTypeToDB(type);
        System.out.println(type.toString());
        int foodId = foodDB.foodTableLength() +1;
        Food customFood = new Food(foodId, foodNameText.getText(), Double.parseDouble(caloriesConsumedText.getText()),
                mealChoiceBox.getValue(), Double.parseDouble(servingSizeText.getText()), foodDatePicker.getValue(),
                        type);
        System.out.println(customFood.toString());
        foodDB.addFoodToDB(customFood);
    }
    /**
     * Function takes data from the user's selected food type and uses it to fill in the form data to then be saved
     * @param actionEvent, confirm search button pressed
     */
    public void fillInSearchDietEntry(javafx.event.ActionEvent actionEvent){
        boolean searchDietDataEntered = checkSearchDietDataEntered();
        if(searchDietDataEntered){
            FoodType entryType = dietSearchCombo.getValue();
            foodNameText.setText(entryType.getFoodDescription());
            servingSizeText.setText(dietSearchServingSizeText.getText());
            // NEED TO CHECK THIS MATHS IS RIGHT
            double calories = (Double.parseDouble(servingSizeText.getText())/100) * entryType.getCaloriesPer100g();
            caloriesConsumedText.setText(Double.toString(calories));
        }

    }

    /**
     * Function to check if diet data capture tools in the GUI have been filled in. If not, changes the boxes colour
     * @return boolean - true if data entered, false if any empty
     */
    public boolean checkDietDataEntered(){
        boolean result = true;
        TextField[] textFields = {foodNameText, caloriesConsumedText, servingSizeText};
        for(TextField tf : textFields){
            if(tf.getText().isEmpty()) {
                tf.setStyle("-fx-background-color: #FF4C4C");
                result = false;
            }
        }
        if(mealChoiceBox.getValue() == null){
            mealChoiceBox.setStyle(("-fx-background-color: #FF4C4C"));
            result=false;
        }
        if(dietSearchCombo.getValue() != null){
            if(dietSearchServingSizeText.getText().isEmpty()){
                dietSearchServingSizeText.setStyle("-fx-background-color: #FF4C4C");
                result = false;
            }
        }
        if(foodDatePicker.getValue() == null){
            foodDatePicker.setStyle(("-fx-background-color: #FF4C4C"));
            result=false;
        }
        return result;

    }

    /**
     * Function to check that, if the user has pressed the button to select a preset list of foods, they have filled
     * in the right data capture boxes in the GUI. If not, boxes change colour
     * @return boolean - true if data entered, false if any empty
     */
    public boolean checkSearchDietDataEntered(){
        boolean result = true;
        if(dietSearchCombo.getValue() == null){
            dietSearchCombo.setStyle(("-fx-background-color: #FF4C4C"));
            result = false;
        }
        if(dietSearchServingSizeText.getText().isEmpty()){
            dietSearchServingSizeText.setStyle("-fx-background-color: #FF4C4C");
            result = false;
        }
        return result;
    }
    /**
     * Function to change GUI data capture boxes colours back to default colours, if they had been changed for not
     * having data in them but now do.
     * @param event - triggered if data capture boxes now have data in them
     */
    @FXML
    public void changeDietColour(Event event){
        final Node node = (Node) event.getSource();
        final String fieldToChange = node.getId();
        switch(fieldToChange){
            case("foodNameText"):
                foodNameText.setStyle("color: revert");
                break;
            case("caloriesConsumedText"):
                caloriesConsumedText.setStyle("color: revert");
                break;
            case("mealChoiceBox"):
                mealChoiceBox.setStyle("color: revert");
                break;
            case("servingSizeText"):
                servingSizeText.setStyle("color: revert");
                break;
            case("foodDatePicker"):
                foodDatePicker.setStyle("color: revert");
                break;
            case("foodSearchCombo"):
                dietSearchCombo.setStyle("color: revert");
                break;
            case("dietSearchServingSizeText"):
                dietSearchServingSizeText.setStyle("color: revert");
                break;
        }
    }

    /*
    to do:
    fix date saving always as 2007???????????????
    validate
    - create dialog boxes to pop up and tell the user to enter a valid date
    - check when entering food or exercise that an exercise of the same name doesnt already exist in the database
        if it does, suggest the user pick from the list instead (same for food) to stop lots of duplicates
        - also, if food or exercise name not already exist in the database then add it for next time
    fix the maths behind the calories per kg per hour maths and the calories per 100g serving maths
    need a "clear" button to clear entry? especially if you've selected from a list and then changed your mind
     */
}












