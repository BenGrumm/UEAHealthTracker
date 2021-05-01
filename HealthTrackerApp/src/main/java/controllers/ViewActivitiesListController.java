package controllers;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Exercise;
import model.ExerciseDBHelper;
import model.ExerciseType;
import testing.ExerciseDBTesting;

import java.time.LocalDate;

public class ViewActivitiesListController extends Controller{

    private static final boolean debug = true;

    public ListView<Exercise> exercisesList;
    public DatePicker dateFrom;
    public DatePicker dateTo;
    public Button dateRangeButton;
    public Label listActivitiesLabel;

    private ObservableList<Exercise> observableList;

    public void initialize(){
        observableList = FXCollections.observableArrayList();

        if(debug){
            ExerciseDBTesting.populateExercisesWithTestData();
        }

        Exercise[] exercises = new ExerciseDBHelper().getAllExercises();

        observableList.addAll(exercises);

        exercisesList.setItems(observableList);
        exercisesList.setCellFactory(exerciseListView -> new ExerciseCellController());
    }

    public void changeDateRange(ActionEvent actionEvent) {
        if(dateFrom.getValue() != null && dateTo.getValue() != null && !dateTo.getValue().isBefore(dateFrom.getValue())){
            Exercise[] exercises = new ExerciseDBHelper().getExercisesWithinRange(dateFrom.getValue(), dateTo.getValue());
            System.out.println(exercises.length);
            observableList.clear();
            observableList.addAll(exercises);
            exercisesList.setItems(observableList);
            listActivitiesLabel.setText("Activities From " + dateFrom.getValue() + " to " + dateTo.getValue());
            dateRangeButton.setText("View Activities");
        }else if(dateFrom.getValue() != null && dateTo.getValue().isBefore(dateFrom.getValue())){
            listActivitiesLabel.setText("Error Date From Is Greater Than Date To");
        }
    }
}
