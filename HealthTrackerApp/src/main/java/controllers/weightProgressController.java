package controllers;

import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.Callback;
import model.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class weightProgressController extends Controller{

    public Label graphLabel;
    public DatePicker dateFrom, dateTo;
    public LineChart weightLineChart;
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

        ArrayList<UserWeight> userWeights = new WeightDBHelper().getUsersWeights(User.getLoggedIn().getID());

        if(userWeights.size() > 0) {
            populateGraphWithRange(userWeights, userWeights.get(User.getLoggedIn().getID()).getDateRecorded(),
                    userWeights.get(userWeights.size() - 1).getDateRecorded());
        }else{
            // TODO put error message or something
            displayErrorNoData();
        }

    }

    public void changeDateRange(ActionEvent actionEvent) {
        /*
        System.out.println("Yes Clicked Here");

        if(dateFrom.getValue() != null && dateTo.getValue() != null && !dateTo.getValue().isBefore(dateFrom.getValue())){
            Exercise[] exercises = new ExerciseDBHelper().getExercisesWithinRange(dateFrom.getValue(), dateTo.getValue());
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
         */
    }

    public void displayErrorNoData(){
        weightLineChart.getData().clear();
        dateRangeButton.setText("No Data Found");
    }

    public void populateGraphWithRange(ArrayList<UserWeight> userWeights, LocalDate xStart, LocalDate xEnd){

        weightLineChart.getData().clear();

        XYChart.Series<String, Double> weights = new XYChart.Series<>();
        XYChart.Series<String, LocalDate> dates = new XYChart.Series<>();

        weights.setName("Weight (kg)");
        dates.setName("Time");

        int daysBetweenDates = (int) ChronoUnit.DAYS.between(xStart, xEnd);
        System.out.println("Days between dates = " + daysBetweenDates + ", num ex = " + userWeights.size());

        if(daysBetweenDates > 0) {

            int arrPosition = 0;
            LocalDate date;
            double weight;

            // For every date in range, set number of mins exercised and calories burned for date
            for (int i = 0; i <= daysBetweenDates; i++) {
                System.out.println(arrPosition);
                if(userWeights != null &&
                        arrPosition < userWeights.size() &&
                        xStart.plusDays(i).equals(userWeights.get(arrPosition).getDateRecorded())){

                    date = userWeights.get(arrPosition).getDateRecorded();
                    weight = userWeights.get(arrPosition).getStones() * 6.35029 + userWeights.get(arrPosition).getPounds() * 0.4536;

                    weights.getData().add(new XYChart.Data<>(Double.toString(weight), weight));
                    dates.getData().add(new XYChart.Data<>(date.toString(), date));
                    arrPosition++;

                }
            }
        }

        weightLineChart.setCreateSymbols(false);

        weightLineChart.getData().add(weights);
        weightLineChart.getData().add(dates);

        weightLineChart.autosize();
        weightLineChart.applyCss();

        weightLineChart.toBack();
    }
}
