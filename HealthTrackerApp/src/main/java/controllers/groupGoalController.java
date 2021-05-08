package controllers;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.*;
import sample.GUI;

public class groupGoalController extends Controller implements Initializable{

    private GoalDBHelper goalDBH = new GoalDBHelper();
    private GroupDBHelper groupDBHelper = new GroupDBHelper();
    private UserDBHelper userDBHelper = new UserDBHelper();
    Group currentGroup;
    ArrayList<Goal> groupsGoals;
    //Add group goal - copied from george

    @FXML
    TextField nameInput;

    @FXML
    TextField targetInput;

    @FXML
    Button addButton;

    @FXML
    Label targetSubscript;

    @FXML
    DatePicker startDate;

    @FXML
    DatePicker calendar;

    @FXML
    private ChoiceBox<String> goalTypeChoice;

    //Based on george add goal
    public void addGroupGoalButton(){
        if(goalTypeChoice.getValue().equals("Weight")){
            goalDBH.addGroupGoal(nameInput.getText(),
                    Goal.goal.WEIGHT,
                    (float)User.loggedIn.getWeightKG(),
                    0.0f,
                    LocalDate.now(),
                    calendar.getValue(),
                    String.valueOf(currentGroup.getiD()),
                    0);
        }else if(goalTypeChoice.getValue().equals("Steps")){
            goalDBH.addGroupGoal(nameInput.getText(),
                    Goal.goal.STEPS,
                    0.0f,
                    Float.valueOf(targetInput.getText()),
                    LocalDate.now(),
                    calendar.getValue(),
                    String.valueOf(currentGroup.getiD()),
                    0);
        }else{
            System.out.println("Something went wrong");
        }
        groupsGoals = goalDBH.getGoalsByGroupId(currentGroup.getiD());

        ArrayList<Integer> userIds = groupDBHelper.getUserIDsSubbed(currentGroup.getiD());

        ArrayList<String> usersEmails = new ArrayList<>();


        for(int x=0;x<userIds.size();x++){
            usersEmails.add(userDBHelper.getUserViaID(userIds.get(x)).getEmail());
        }

        String[] userAddresses = new String[usersEmails.size()];

        for(int x=0;x<userIds.size();x++){
            userAddresses[x] = usersEmails.get(x);
            System.out.println(usersEmails);
        }

        Email.emailGroupAboutNewGoal(currentGroup,userAddresses);

        if(groupsGoals.size() == 3){
            goToGroupPage();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addButton.setDisable(true);

        goalTypeChoice.setValue("Weight");
        startDate.setVisible(true);
        calendar.setVisible(true);
        ObservableList<String> selections = FXCollections.observableArrayList("Weight", "Steps");
        goalTypeChoice.setItems(selections);
        targetSubscript.setText("kg to lose");
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
            if(t1.equals("") || targetInput.getText().equals("") || (calendar.getValue()== null && startDate.isVisible() == true && calendar.isVisible() == true)){
                addButton.setDisable(true);
            }else{
                addButton.setDisable(false);
            }
        });

        goalTypeChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.equals("Weight")){
                    startDate.setVisible(true);
                    calendar.setVisible(true);
                    targetSubscript.setText("kg to lose");
                    nameInput.setDisable(false);
                }else{
                    startDate.setVisible(true);
                    calendar.setVisible(true);
                    targetSubscript.setText("Steps");
                    nameInput.setDisable(false);
                }
            }
        });

        nameInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.equals("") || targetInput.getText().equals("") || (calendar.getValue()== null && startDate.isVisible() == true && calendar.isVisible() == true)){
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
                }else if(t1.equals("") || nameInput.getText().equals("")|| (calendar.getValue()== null && startDate.isVisible() == true && calendar.isVisible() == true)){
                    addButton.setDisable(true);
                }else{
                    addButton.setDisable(false);
                }
            }
        });

    }
}
