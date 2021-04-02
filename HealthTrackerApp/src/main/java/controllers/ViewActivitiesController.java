package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.Callback;
import model.Exercise;
import model.ExerciseDBHelper;
import model.ExerciseType;

import java.time.LocalDate;

public class ViewActivitiesController extends Controller{

    public Label graphLabel;
    public DatePicker dateFrom, dateTo;
    public LineChart exerciseLineChart;

    public void initialize() {
        System.out.println("Init");
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

        // Exercise[] allExercises = new ExerciseDBHelper().getAllExercises();
        // TODO switch to getting db exercises
        Exercise[] allExercises = {new Exercise(0, 12, 150, new ExerciseType(), LocalDate.now()),
        new Exercise(1, 15, 120, new ExerciseType(), LocalDate.now().minusDays(13)),
        new Exercise(2, 15, 120, new ExerciseType(), LocalDate.now()),
        new Exercise(3, 20, 250, new ExerciseType(), LocalDate.now().plusDays(5))};

        XYChart.Series<String, Double> series1 = new XYChart.Series<>();
        XYChart.Series<String, Double> series2 = new XYChart.Series<>();

        series1.setName("Mins Exercised");
        series2.setName("Calories Burned");

        if(allExercises != null){
            for(Exercise e : allExercises){
                series1.getData().add(new XYChart.Data<>(e.getDate().toString(), e.getMinutesExercised()));
                series2.getData().add(new XYChart.Data<>(e.getDate().toString(), e.getCaloriesBurned()));
            }
        }

        exerciseLineChart.getData().add(series1);
        exerciseLineChart.getData().add(series2);

    }

    public void changeDateRange(ActionEvent actionEvent) {
        System.out.println("Yes Clicked Here");

        if(dateFrom.getValue() != null && dateTo.getValue() != null){
            System.out.println(java.sql.Date.valueOf(dateFrom.getValue()));
            System.out.println(java.sql.Date.valueOf(dateTo.getValue()));
        }
        graphLabel.setText("Label Set");
    }

}
