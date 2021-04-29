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
import java.time.temporal.ChronoUnit;

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

        Exercise[] allExercises = new ExerciseDBHelper().getAllExercises();
        // TODO switch to getting db exercises
        // in ascending order from smallest date to largest
//        ExerciseType et = new ExerciseType(2, "Running Fast", 12);
//        Exercise[] allExercises = {new Exercise(1, 15, 120, et, LocalDate.now().minusDays(13)),
//                new Exercise(0, 12, 150, et, LocalDate.now()),
//                new Exercise(2, 15, 120, et, LocalDate.now()),
//                new Exercise(3, 20, 250, et, LocalDate.now().plusDays(5))};

        if(allExercises.length > 0) {
            populateGraphWithRange(allExercises, allExercises[0].getDate(), allExercises[allExercises.length - 1].getDate());
        }else{

        }

    }

    public void changeDateRange(ActionEvent actionEvent) {
        System.out.println("Yes Clicked Here");

        if(dateFrom.getValue() != null && dateTo.getValue() != null){
            Exercise[] exercises = new ExerciseDBHelper().getExercisesWithinRange(dateFrom.getValue(), dateTo.getValue());

        }
        graphLabel.setText("Label Set");
    }

    public void populateGraphWithRange(Exercise[] exercises, LocalDate xStart, LocalDate xEnd){
        exerciseLineChart.getData().clear();

        XYChart.Series<String, Double> minsExercise = new XYChart.Series<>();
        XYChart.Series<String, Double> calsBurned = new XYChart.Series<>();

        minsExercise.setName("Exercise Time (m)");
        calsBurned.setName("Calories Burned");

        int daysBetweenDates = (int) ChronoUnit.DAYS.between(xStart, xEnd);

        if(daysBetweenDates > 0) {
            int arrPosition = 0;
            LocalDate date;
            double minutesExercised;
            double caloriesBurned;
            // For every date in range, set number of mins exercised and calories burned for date
            for (int i = 0; i <= daysBetweenDates; i++) {
                System.out.println(arrPosition);
                if(exercises != null && arrPosition < exercises.length && xStart.plusDays(i).equals(exercises[arrPosition].getDate())){
                    date = exercises[arrPosition].getDate();
                    caloriesBurned = exercises[arrPosition].getCaloriesBurned();
                    minutesExercised = exercises[arrPosition].getMinutesExercised();

                    // Only want one data point for every day so check if multiple entries for current day
                    while((arrPosition + 1) < exercises.length && date.equals(exercises[arrPosition + 1].getDate())){
                        System.out.println("In while = " + arrPosition);
                        arrPosition++;
                        caloriesBurned += exercises[arrPosition].getCaloriesBurned();
                        minutesExercised += exercises[arrPosition].getMinutesExercised();
                    }

                    minsExercise.getData().add(new XYChart.Data<>(date.toString(), minutesExercised));
                    calsBurned.getData().add(new XYChart.Data<>(date.toString(), caloriesBurned));
                    arrPosition++;
                }else{
                    minsExercise.getData().add(new XYChart.Data<>(xStart.plusDays(i).toString(), 0.0));
                    calsBurned.getData().add(new XYChart.Data<>(xStart.plusDays(i).toString(), 0.0));
                }
            }
        }

        exerciseLineChart.getData().add(minsExercise);
        exerciseLineChart.getData().add(calsBurned);
    }

}
