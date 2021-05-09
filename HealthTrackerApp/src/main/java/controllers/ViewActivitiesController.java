package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.Callback;
import model.Exercise;
import model.ExerciseDBHelper;
import model.ExerciseType;
import model.User;
import sample.GUI;
import sample.Main;
import testing.ExerciseDBTesting;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ViewActivitiesController extends Controller{

    public Label graphLabel;
    public DatePicker dateFrom, dateTo;
    public LineChart exerciseLineChart;
    public Button dateRangeButton;

    public void initialize() {
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

        //Add exercised to db
        if(Main.debug){
            ExerciseDBTesting.populateExercisesWithTestData();
        }

        Exercise[] allExercises = new ExerciseDBHelper().getAllExercises();

        if(allExercises.length > 0) {
            populateGraphWithRange(allExercises, allExercises[0].getDate(), allExercises[allExercises.length - 1].getDate());
        }else{
            // TODO put error message or something
            displayErrorNoData();
        }

    }

    public void changeDateRange(ActionEvent actionEvent) {
        System.out.println("Yes Clicked Here");

        if(dateFrom.getValue() != null && dateTo.getValue() != null && !dateTo.getValue().isBefore(dateFrom.getValue())){
            Exercise[] exercises = new ExerciseDBHelper().getExercisesWithinRange(dateFrom.getValue(), dateTo.getValue(), ExerciseDBHelper.Order.ASC);
            System.out.println(exercises.length);
            if(exercises.length != 0) {
                populateGraphWithRange(exercises, dateFrom.getValue(), dateTo.getValue());
            }else {
                displayErrorNoData();
            }
            graphLabel.setText("Activities From " + dateFrom.getValue() + " to " + dateTo.getValue());
            dateRangeButton.setText("View Activities");
        }else if(dateFrom.getValue() != null && dateTo.getValue().isBefore(dateFrom.getValue())){
            graphLabel.setText("Error Date From Is Greater Than Date To");
        }
    }

    public void displayErrorNoData(){
        exerciseLineChart.getData().clear();
        dateRangeButton.setText("No Data Found");
    }

    public void populateGraphWithRange(Exercise[] exercises, LocalDate xStart, LocalDate xEnd){
        exerciseLineChart.getData().clear();

        XYChart.Series<String, Double> minsExercise = new XYChart.Series<>();
        XYChart.Series<String, Double> calsBurned = new XYChart.Series<>();

        minsExercise.setName("Exercise Time (m)");
        calsBurned.setName("Calories Burned");

        int daysBetweenDates = (int) ChronoUnit.DAYS.between(xStart, xEnd);
        System.out.println("Days between dates = " + daysBetweenDates + ", num ex = " + exercises.length);

        if(daysBetweenDates > 0) {
            int arrPosition = 0;
            LocalDate date;
            double minutesExercised;
            double caloriesBurned;
            // For every date in range, set number of mins exercised and calories burned for date
            for (int i = 0; i <= daysBetweenDates; i++) {
                System.out.println(arrPosition);
                if(     exercises != null &&
                        arrPosition < exercises.length &&
                        xStart.plusDays(i).equals(exercises[arrPosition].getDate())){
                    System.out.println("Inside");
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

        exerciseLineChart.setCreateSymbols(false);

        exerciseLineChart.getData().add(minsExercise);
        exerciseLineChart.getData().add(calsBurned);

        exerciseLineChart.autosize();
        exerciseLineChart.applyCss();

        exerciseLineChart.toBack();
    }

    public void switchToListActivity(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_activities_list.fxml"));

        try {
            Parent root = loader.load();
            GUI.changeScene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
