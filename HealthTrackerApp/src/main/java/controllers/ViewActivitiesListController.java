package controllers;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import model.Exercise;
import model.ExerciseType;

import java.time.LocalDate;

public class ViewActivitiesListController extends Controller{

    public ListView<Exercise> exercisesList;

    private ObservableList<Exercise> observableList;

    public void initialize(){
        observableList = FXCollections.observableArrayList();

        ExerciseType et = new ExerciseType(2, "Running Fast", 12);
        ExerciseType et2 = new ExerciseType(5, "Running Slow", 12);
        ExerciseType et3 = new ExerciseType(22, "Canoeing", 12);
        ExerciseType et4 = new ExerciseType(25, "Football", 12);

        observableList.addAll(
                new Exercise(1, 15, 12, et, LocalDate.now()),
                new Exercise(2, 18, 1400, et4, LocalDate.now()),
                new Exercise(3, 22, 150, et3, LocalDate.now()),
                new Exercise(4, 66, 1, et2, LocalDate.now()),
                new Exercise(5, 12, 122, et3, LocalDate.now()));

        exercisesList.setItems(observableList);
        exercisesList.setCellFactory(exerciseListView -> new ExerciseCellController());
    }

}
