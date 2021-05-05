package controllers;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.Goal;
import model.GoalDBHelper;
import model.User;
import model.UserDBHelper;

public class goalController extends Controller{

    GoalDBHelper goalDBHelper;

    @FXML
    VBox goalsPane;

    @FXML
    TextField nameInput;

    @FXML
    TextField targetInput;

    @FXML
    Button addButton;

    @FXML
    private ChoiceBox<String> goalTypeChoice;

    public void addGoal(){
        for(Goal goal : goalDBHelper.getGoalsByUserId(User.loggedIn.getID())){
            goal.renderGoal(goalsPane);
        }
    }

    public void initialize() {
        goalDBHelper = new GoalDBHelper();
        //goalDBHelper.getGoalByID(1).renderGoal(goalsPane);
        //goalDBHelper.getGoalByID(2).renderGoal(goalsPane);

        addButton.setDisable(true);

        goalTypeChoice.setValue("Weight");
        ObservableList<String> selections = FXCollections.observableArrayList("Weight", "Calorie", "Steps");
        goalTypeChoice.setItems(selections);


        nameInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.equals("") || targetInput.getText().equals("")){
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
                }else if(t1.equals("") || nameInput.getText().equals("")){
                    addButton.setDisable(true);
                }else{
                    addButton.setDisable(false);
                }
            }
        });



        UserDBHelper userDBHelper = new UserDBHelper();

        /*for(Goal goal : goalDBHelper.getGoalsByUserId(userDBHelper.getUserViaID(2))){
            goal.renderGoal(goalsPane);
        }*/
        //fetch goals info from table

        //Create goal objects from goal info

        //Display goal objects in goals pane.


        //THIS WILL BE DONE YES

    }
}
