package controllers;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.Goal;
import model.GoalDBHelper;
import model.UserDBHelper;

public class goalController extends Controller{

    @FXML
    VBox goalsPane;
    public void initialize() {
        GoalDBHelper goalDBHelper = new GoalDBHelper();
        goalDBHelper.getGoalByID(1).renderGoal(goalsPane);
        goalDBHelper.getGoalByID(2).renderGoal(goalsPane);

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
