package controllers;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.Exercise;
import model.ExerciseDBHelper;
import model.ExerciseType;
import sample.GUI;
import sample.Main;
import testing.ExerciseDBTesting;

import java.io.IOException;
import java.time.LocalDate;

public class ViewActivitiesListController extends Controller{

    public ListView<Exercise> exercisesList;
    public DatePicker dateFrom;
    public DatePicker dateTo;
    public Button dateRangeButton;
    public Label listActivitiesLabel;

    private ObservableList<Exercise> observableList;

    public void initialize(){
        // Set Selectable days to be from current day and before
        Callback<DatePicker, DateCell> cb = d -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(LocalDate.now()));
            }
        };

        dateFrom.setDayCellFactory(cb);
        dateTo.setDayCellFactory(cb);

        observableList = FXCollections.observableArrayList();

        if(Main.debug){
            ExerciseDBTesting.populateExercisesWithTestData();
        }

        Exercise[] exercises = new ExerciseDBHelper().getAllExercises(ExerciseDBHelper.Order.DESC);

        observableList.addAll(exercises);

        exercisesList.setItems(observableList);
        exercisesList.setCellFactory(exerciseListView -> new ExerciseCellController());
    }

    public void changeDateRange(ActionEvent actionEvent) {
        if(dateFrom.getValue() != null && dateTo.getValue() != null && !dateTo.getValue().isBefore(dateFrom.getValue())){
            Exercise[] exercises = new ExerciseDBHelper().getExercisesWithinRange(dateFrom.getValue(), dateTo.getValue(), ExerciseDBHelper.Order.DESC);
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

    public void switchToGraphActivity(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_activities.fxml"));
        try {
            Parent root = loader.load();
            GUI.changeScene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
