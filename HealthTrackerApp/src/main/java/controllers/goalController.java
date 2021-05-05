package controllers;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.Goal;
import model.GoalDBHelper;
import model.User;
import model.UserDBHelper;

import java.time.LocalDate;

public class goalController extends Controller{

    GoalDBHelper goalDBHelper;

    @FXML
    VBox goalsPane;

    @FXML
    TextField nameInput;

    @FXML
    TextField targetInput;

    @FXML
    Label currentWeight;

    @FXML
    Label currentHeight;

    @FXML
    Button addButton;

    @FXML
    Label targetSubscript;

    @FXML
    DatePicker startDate;

    @FXML
    DatePicker calendar;

    @FXML
    VBox dateSelection;

    @FXML
    private ChoiceBox<String> goalTypeChoice;

    public void addGoal(){
        //System.out.println("Target input: " + targetInput.getText());
        //System.out.println("Date input: " + calendar.getValue());
        if(goalTypeChoice.getValue().equals("Calorie")){
            //goalDBHelper.addGoal(nameInput.getText(), Goal.goal.valueOf(goalTypeChoice.getValue()), )
            for (Goal goal: goalDBHelper.getGoalsByUserId(User.loggedIn.getID())) {
                if(goal.getGoalType() == Goal.goal.DIET){
                    goalDBHelper.removeGoal(goal.getGoalID());
                }

            }
            User.dailyCalorieLimit = Integer.valueOf(targetInput.getText());
            goalDBHelper.addGoal("Daily calories",
                    Goal.goal.DIET,
                    0.0f,
                    Float.valueOf(targetInput.getText()),
                    String.valueOf(User.loggedIn.getID()),
                    0);
        }else if(goalTypeChoice.getValue().equals("Weight")){
            goalDBHelper.addGoal(nameInput.getText(),
                    Goal.goal.WEIGHT,
                    (float)User.loggedIn.getWeightKG(),
                    Float.valueOf(targetInput.getText()),
                    LocalDate.now(),
                    calendar.getValue(),
                    String.valueOf(User.loggedIn.getID()),
                    0);
        }else if(goalTypeChoice.getValue().equals("Steps")){
            goalDBHelper.addGoal(nameInput.getText(),
                    Goal.goal.STEPS,
                    0.0f,
                    Float.valueOf(targetInput.getText()),
                    LocalDate.now(),
                    calendar.getValue(),
                    String.valueOf(User.loggedIn.getID()),
                    0);
        }else{
            System.out.println("Something went wrong");
        }
        renderGoals();
    }

    public void renderGoals(){
        goalsPane.getChildren().clear();
        for(Goal goal : goalDBHelper.getGoalsByUserId(User.loggedIn.getID())){
            goal.renderGoal(goalsPane);
        }
    }

    public void initialize() {
        goalDBHelper = new GoalDBHelper();
        //goalDBHelper.getGoalByID(1).renderGoal(goalsPane);
        //goalDBHelper.getGoalByID(2).renderGoal(goalsPane);
        renderGoals();
        addButton.setDisable(true);

        goalTypeChoice.setValue("Weight");
        dateSelection.setVisible(true);
        ObservableList<String> selections = FXCollections.observableArrayList("Weight", "Calorie", "Steps");
        goalTypeChoice.setItems(selections);
        targetSubscript.setText("kg");
        //startDate.setText(LocalDate.now().toString());
        startDate.setValue(LocalDate.now());
        startDate.setDisable(true);

        //System.out.println(User.dailyCalorieLimit);

        calendar.setDayCellFactory(datePicker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) < 0 );
            }
        });

        calendar.valueProperty().addListener((observableValue, s, t1) -> {
            if(t1.equals("") || targetInput.getText().equals("") || (calendar.getValue()== null && dateSelection.isVisible() == true)){
                addButton.setDisable(true);
            }else{
                addButton.setDisable(false);
            }
        });

        goalTypeChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.equals("Weight")){
                    dateSelection.setVisible(true);
                    targetSubscript.setText("kg");
                    nameInput.setDisable(false);
                }else if(t1.equals("Calorie")){
                    dateSelection.setVisible(false);
                    nameInput.setText("Daily Calories");
                    nameInput.setDisable(true);
                    targetSubscript.setText("Calories");
                }else{
                    dateSelection.setVisible(true);
                    targetSubscript.setText("Steps");
                    nameInput.setDisable(false);
                }
            }
        });

        nameInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.equals("") || targetInput.getText().equals("") || (calendar.getValue()== null && dateSelection.isVisible() == true)){
                    addButton.setDisable(true);
                }else{
                    addButton.setDisable(false);
                }
            }
        });

        targetInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    targetInput.setText(t1.replaceAll("[^\\d]", ""));
                }else if(t1.equals("") || nameInput.getText().equals("")|| (calendar.getValue()== null && dateSelection.isVisible() == true)){
                    addButton.setDisable(true);
                }else{
                    addButton.setDisable(false);
                }
            }
        });

        UserDBHelper userDBHelper = new UserDBHelper();

        currentWeight.setText(String.format("%.2fkg", User.loggedIn.getWeightKG()));
        currentHeight.setText(String.format("%.0fcm", User.loggedIn.getHeight()));

        /*for(Goal goal : goalDBHelper.getGoalsByUserId(userDBHelper.getUserViaID(2))){
            goal.renderGoal(goalsPane);
        }*/
        //fetch goals info from table

        //Create goal objects from goal info

        //Display goal objects in goals pane.


        //THIS WILL BE DONE YES

    }
}
