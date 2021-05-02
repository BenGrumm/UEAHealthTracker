package controllers;

import javafx.event.ActionEvent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.Callback;

import model.*;

import javax.sound.sampled.Line;
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
        for (int x=0; x<userWeights.size(); x++){
            System.out.println(userWeights.get(x).getDateRecorded());
        }

        /*
        if(userWeights.size() > 0) {
            populateGraphWithRange(userWeights, userWeights.get(User.getLoggedIn().getID()).getDateRecorded(),
                    userWeights.get(userWeights.size() - 1).getDateRecorded());
        }else{
            // TODO put error message or something
            displayErrorNoData();
        }
        */

        //Maybe default to first weight record and todays date.
        LocalDate startDate = LocalDate.parse("2021-05-03");
        LocalDate endDate = LocalDate.parse("2021-05-07");

        PopGraphInRange(userWeights,startDate, endDate);


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


    /**
     * This method is used to plot the weight of the user in pounds, against the date in days.
     * @param userWeights List of all usersWeights in record.
     * @param startDate date to display from
     * @param endDate date to display to.
     */
    public void PopGraphInRange(ArrayList<UserWeight> userWeights, LocalDate startDate, LocalDate endDate) {

        //Clears the data being displayed graph.
        weightLineChart.getData().clear();

        //Creates an arraylist storing the every date between the startDate and endDate.
        ArrayList<LocalDate> dates = new ArrayList<>();
        LocalDate aDate = startDate;
        while (aDate.isBefore(endDate)) {
            dates.add(aDate);
            aDate = aDate.plusDays(1);
        }
        dates.add(endDate);

        //Create a series to plot
        XYChart.Series series = new XYChart.Series();

        int prevWeight = 0;
        int userWeightsCounter = 0;

        //sets the previous weight to the most recent weight before the startDate, and increases the index to the
        // users weight record in dates to the one on or after the start date.
        while (userWeights.get(userWeightsCounter).getDateRecorded().isBefore(startDate)) {
            prevWeight = (userWeights.get(userWeightsCounter).getStones()*14) + userWeights.get(userWeightsCounter).getPounds();
            userWeightsCounter++;
        }

        //iterates through all the dates in the list and plots the most  recent weight.

        for (int x = 0; x < dates.size(); x++) {
            String dateAsString = dates.get(x).toString();

            //If the date is less than the most recent date record
            if(userWeightsCounter < userWeights.size()) {
                //if the date of the weight in record is equal to the date in dates, add to plot
                if (userWeights.get(userWeightsCounter).getDateRecorded().equals(dates.get(x))) {
                    int weight = (userWeights.get(userWeightsCounter).getStones() * 14) + userWeights.get(userWeightsCounter).getPounds();
                    series.getData().add(new XYChart.Data(dateAsString, weight));
                    prevWeight = weight;
                    userWeightsCounter++;
                }
                //else add most recent previous dates weight
                else {
                    series.getData().add(new XYChart.Data(dateAsString, prevWeight));
                }
            }
            //else add most recent previous date weight
            else{
                series.getData().add(new XYChart.Data(dateAsString, prevWeight));
            }

        }

        series.setName(User.getLoggedIn().getFirstName() + "'s Weight Over Time");
        weightLineChart.getData().add(series);
    }
}
